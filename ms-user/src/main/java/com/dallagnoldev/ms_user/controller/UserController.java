package com.dallagnoldev.ms_user.controller;

import com.dallagnoldev.ms_user.dto.*;
import com.dallagnoldev.ms_user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        UserResponse user = service.register(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.userId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = service.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID userId) {
        UserResponse user = service.findUser(userId);

        return ResponseEntity.ok(user);
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> findAllUsers(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> users = service.findAllUsers(pageable);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/users/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<LoginResponse> verifyAccount(
            @RequestBody @Valid VerifyUserRequest request) {

        LoginResponse token = service.verify(request.email(), request.code());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<Void> resendCode(@RequestBody @Valid ResendCodeRequest request) {
        service.requestNewCode(request);
        return ResponseEntity.noContent().build();
    }
}
