package com.dallagnoldev.ms_user.dto;

import com.dallagnoldev.ms_user.enums.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String name,
        String email,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
