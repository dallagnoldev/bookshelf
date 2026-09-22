package com.dallagnoldev.ms_user.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message, Integer status
) {
}
