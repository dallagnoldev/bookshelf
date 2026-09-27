package com.dallagnoldev.ms_books.dto;

import com.dallagnoldev.ms_books.model.BookModel;

import java.util.UUID;

public record LoanRequest(
        UUID userId,
        UUID bookId
) {
}
