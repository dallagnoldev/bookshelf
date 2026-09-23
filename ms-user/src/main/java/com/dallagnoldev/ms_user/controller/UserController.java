package com.dallagnoldev.ms_user.controller;

import com.dallagnoldev.ms_user.dto.UserRequest;
import com.dallagnoldev.ms_user.dto.UserResponse;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        UserResponse user = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.userId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID userId) {
        UserResponse user = service.findUser(userId);

        return ResponseEntity.ok(user);
    }
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> findAllUsers(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> users = service.findAllUsers(pageable);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
