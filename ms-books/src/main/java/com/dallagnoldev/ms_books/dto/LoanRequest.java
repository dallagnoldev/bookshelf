package com.dallagnoldev.ms_books.dto;

import com.dallagnoldev.ms_books.model.BookModel;

import java.util.UUID;

public record LoanRequest(
        String userEmail,
        UUID bookId
) {
}
