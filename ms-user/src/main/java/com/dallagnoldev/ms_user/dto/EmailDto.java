package com.dallagnoldev.ms_user.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailDto(
        UUID userId,
        String emailTo,
        String subject,
        String text
) {
}
