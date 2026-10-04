package com.dallagnoldev.ms_books.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record BookResponse(
        UUID bookId,
        String title,
        String author,
        String isbn,
        String publisher,
        Integer publicationYear,
        Integer totalCopies,
        Integer availableCopies,
        String category,
        String description,
        String language,
        String aiReview,
        Instant createdAt,
        Instant updatedAt
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
