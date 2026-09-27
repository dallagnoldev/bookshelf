package com.dallagnoldev.ms_books.service;

import com.dallagnoldev.ms_books.dto.BookRequest;
import com.dallagnoldev.ms_books.dto.BookResponse;
import com.dallagnoldev.ms_books.exception.BookException;
import com.dallagnoldev.ms_books.model.BookModel;
import com.dallagnoldev.ms_books.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final IBookRepository bookRepository;

    @Transactional
    public BookResponse createBook(BookRequest request) {

        String isbnFormatted = request.isbn().trim().replace("-", "");

        if (bookRepository.existsByIsbn(isbnFormatted)) {
            throw new BookException("Book with ISBN: " + isbnFormatted + " already exists");
        }

        BookModel bookModel = BookModel.builder()
                .title(request.title().trim())
                .author(request.author().trim())
                .isbn(isbnFormatted)
                .publisher(request.publisher().trim())
                .publicationYear(request.publicationYear())
                .totalCopies(request.totalCopies())
                .availableCopies(request.availableCopies())
                .category(request.category().trim())
                .description(request.description().trim())
                .language(request.language().trim())
                .build();

        BookModel savedBook = bookRepository.save(bookModel);

        return toResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public BookResponse findBook(UUID id) {
        BookModel book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID: " + id + " not found"));

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findAllBooks(String title, String author, String publisher, String isbn, String category, Pageable pageable) {
        String titleParam = (title != null && !title.isBlank()) ? "%" + title + "%" : null;
        String authorParam = (author != null && !author.isBlank()) ? "%" + author + "%" : null;
        String publisherParam = (publisher != null && !publisher.isBlank()) ? "%" + publisher + "%" : null;
        String isbnParam = (isbn != null && !isbn.isBlank()) ? isbn + "%" : null;
        String categoryParam = (category != null && category.isBlank()) ? "%" + category + "%" : null;

        Page<BookModel> books = bookRepository.findByFilters(titleParam, authorParam, publisherParam, isbnParam, categoryParam, pageable);

        return books.map(this::toResponse);
    }

    @Transactional
    public void deleteBookById(UUID id) {
        BookModel book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID: " + id + " not found"));

        bookRepository.delete(book);
    }

    public BookResponse toResponse(BookModel bookModel) {
        return new BookResponse(
                bookModel.getBookId(),
                bookModel.getTitle(),
                bookModel.getAuthor(),
                bookModel.getIsbn(),
                bookModel.getPublisher(),
                bookModel.getPublicationYear(),
                bookModel.getTotalCopies(),
                bookModel.getAvailableCopies(),
                bookModel.getCategory(),
                bookModel.getDescription(),
                bookModel.getLanguage(),
                bookModel.getAiReview(),
                Instant.now(),
                Instant.now()
        );
    }
}
