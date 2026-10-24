package com.dallagnoldev.ms_user.service;

import com.dallagnoldev.ms_user.dto.LoginRequest;
import com.dallagnoldev.ms_user.dto.LoginResponse;
import com.dallagnoldev.ms_user.dto.UserRequest;
import com.dallagnoldev.ms_user.dto.UserResponse;
import com.dallagnoldev.ms_user.enums.UserRoles;
import com.dallagnoldev.ms_user.enums.UserStatus;
import com.dallagnoldev.ms_user.exception.EmailException;
import com.dallagnoldev.ms_user.exception.NotFoundException;
import com.dallagnoldev.ms_user.exception.VerificationCodeException;
import com.dallagnoldev.ms_user.model.RoleModel;
import com.dallagnoldev.ms_user.model.UserModel;
import com.dallagnoldev.ms_user.producer.UserProducer;
import com.dallagnoldev.ms_user.repository.IRoleRepository;
import com.dallagnoldev.ms_user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProducer userProducer;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String VERIFICATION_CODE_KEY_PREFIX="verification:email:";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public UserResponse register(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailException("Email already exists");
        }

        String code = generateVerificationCode();

        String redisKey = VERIFICATION_CODE_KEY_PREFIX + request.email();

        stringRedisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(15));

        RoleModel role = roleRepository.findRoleByName(UserRoles.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Role not Found"));

        UserModel userModel = UserModel.builder()
                .name(request.name().trim())
                .email(request.email().trim())
                .password(passwordEncoder.encode(request.password().trim()))
                .status(UserStatus.UNVERIFIED)
                .roles(Set.of(role))
                .build();

        UserModel userSaved = userRepository.save(userModel);

        userProducer.publishMessageEmail(userSaved, code);
        return toResponse(userSaved);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(authToken);
        UserModel user = (UserModel) authentication.getPrincipal();

        if (user == null) {
            throw new NotFoundException("The account doesn't exist");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponse(token);
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(UUID userId) {
        UserModel usermodel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return toResponse(usermodel);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        Page<UserModel> users = userRepository.findAll(pageable);

        if (users.getTotalElements() == 0) {
            throw new NotFoundException("No users found");
        }

        return users.map(this::toResponse);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        UserModel usermodel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(usermodel);
    }

    public UserResponse toResponse(UserModel userModel) {
        return new UserResponse(
                userModel.getUserId(),
                userModel.getName(),
                userModel.getEmail(),
                userModel.getStatus(),
                Instant.now(),
                Instant.now()
        );
    }

    public String generateVerificationCode() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    @Transactional
    public LoginResponse verify(String email, String codeInput) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getStatus() == UserStatus.VERIFIED) {
            throw new VerificationCodeException("Your account is already verified");
        }

        String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
        String savedCode = stringRedisTemplate.opsForValue().get(redisKey);

        if (savedCode == null) {
            throw new VerificationCodeException("Verification code expired");
        }

        if (!savedCode.equals(codeInput)) {
            throw new VerificationCodeException("Invalid verification code");
        }

        stringRedisTemplate.delete(redisKey);

        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);

        String token = tokenService.generateToken(user);

        return new LoginResponse(token);
    }

}
