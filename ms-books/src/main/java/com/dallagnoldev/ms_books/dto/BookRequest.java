package com.dallagnoldev.ms_books.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.ISBN;

public record BookRequest(
        @NotBlank @Size(min = 4, message = "Book title must be at least 4 characters") String title,
        @NotBlank @Size(min = 4, message = "Book author name must be at least 4 characters") String author,
        @NotBlank @ISBN(message = "Invalid ISBN format") String isbn,
        @NotBlank(message = "Publisher name is mandatory") String publisher,
        @NotNull(message = "Publication year is mandatory") @Positive Integer publicationYear,
        @NotBlank(message = "Total copies quantity is mandatory") @PositiveOrZero Integer totalCopies,
        @NotBlank(message = "Available copies quantity is mandatory") @PositiveOrZero Integer availableCopies,
        @NotBlank(message = "Book category is mandatory") String category,
        @NotBlank(message = "Book description is mandatory") String description,
        @NotBlank(message = "Book language is mandatory") String language
) {
}
