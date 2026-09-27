package com.dallagnoldev.ms_books.dto;

import com.dallagnoldev.ms_books.enums.LoanStatus;
import com.dallagnoldev.ms_books.model.BookModel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record LoanResponse(
        UUID loanId,
        UUID userId,
        BookModel book,
        LocalDate loanDate,
        LocalDate returnLoanDate,
        LoanStatus loanStatus,
        Instant createdAt
) {
}
