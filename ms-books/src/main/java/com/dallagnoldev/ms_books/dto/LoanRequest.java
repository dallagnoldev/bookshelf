package com.dallagnoldev.ms_books.dto;

import java.util.UUID;

public record LoanRequest(
        UUID bookId
) {
}
