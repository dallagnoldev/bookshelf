package com.dallagnoldev.ms_books.dto;

import lombok.Builder;

@Builder
public record EmailDto(
        String emailTo,
        String subject,
        String text
) {
}
