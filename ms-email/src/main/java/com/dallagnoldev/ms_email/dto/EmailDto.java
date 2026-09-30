package com.dallagnoldev.ms_email.dto;

public record EmailDto(
        String emailTo,
        String subject,
        String text
) {
}
