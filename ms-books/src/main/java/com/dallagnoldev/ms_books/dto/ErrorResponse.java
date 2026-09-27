package com.dallagnoldev.ms_books.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message,
        Integer status
) {
}
