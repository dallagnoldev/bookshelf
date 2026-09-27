package com.dallagnoldev.ms_books.controller;

import com.dallagnoldev.ms_books.dto.BookRequest;
import com.dallagnoldev.ms_books.dto.BookResponse;
import com.dallagnoldev.ms_books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        BookResponse bookResponse = bookService.createBook(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookResponse.bookId())
                .toUri();

        return ResponseEntity.created(location).body(bookResponse);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> findBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String category,
            @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookResponse> books = bookService.findAllBooks(title, author, publisher, isbn, category, pageable);

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
