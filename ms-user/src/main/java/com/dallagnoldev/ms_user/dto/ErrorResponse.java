package com.dallagnoldev.ms_user.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message, Integer status
) {
}
