package com.dallagnoldev.ms_user.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String name,
        String email,
        String password,
        Instant createdAt,
        Instant updatedAt
) {
}
