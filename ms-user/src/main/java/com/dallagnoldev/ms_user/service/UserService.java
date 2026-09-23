package com.dallagnoldev.ms_user.service;

import com.dallagnoldev.ms_user.dto.UserRequest;
import com.dallagnoldev.ms_user.dto.UserResponse;
import com.dallagnoldev.ms_user.enums.UserRoles;
import com.dallagnoldev.ms_user.enums.UserStatus;
import com.dallagnoldev.ms_user.exception.EmailException;
import com.dallagnoldev.ms_user.exception.NotFoundException;
import com.dallagnoldev.ms_user.model.RoleModel;
import com.dallagnoldev.ms_user.model.UserModel;
import com.dallagnoldev.ms_user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailException("Email already exists");
        }

        UserModel userModel = UserModel.builder()
                .name(request.name().trim())
                .email(request.email().trim())
                .password(passwordEncoder.encode(request.password().trim()))
                .status(UserStatus.UNVERIFIED)
                .roles(Set.of(RoleModel.builder().name(UserRoles.ROLE_USER.name()).build()))
                .build();

        UserModel userSaved = userRepository.save(userModel);

        return toResponse(userSaved);
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
                userModel.getCreatedAt(),
                userModel.getUpdatedAt()
        );
    }
}
