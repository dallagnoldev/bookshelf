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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final IBookRepository bookRepository;

    @Transactional
    public BookResponse createBook(BookRequest request) {

        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BookException("Book with ISBN: " + request.isbn() + " already exists");
        }

        BookModel bookModel = BookModel.builder()
                .title(request.title().trim())
                .author(request.author().trim())
                .isbn(request.isbn().trim().replace("-", ""))
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
    public BookResponse findBookById(UUID id) {
        BookModel book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID: " + id + " not found"));

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse findBookByIsbn(String isbn) {
        String isbnFormatted = isbn.replace("-", "").trim();

        if (isbnFormatted.length() != 13) {
            throw new BookException("Invalid ISBN format");
        }

        BookModel book = bookRepository.findByIsbn(isbnFormatted)
                .orElseThrow(() -> new BookException("Book with ISBN: " + isbnFormatted + " not found"));

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse findBookByTitle(String title) {
        BookModel book = bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookException("Book with title: " + title + " not found"));

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findByAuthor(String author, Pageable pageable) {
        Page<BookModel> books = bookRepository.findByAuthor(author, pageable)
                .orElseThrow(() -> new BookException("Author: " + author + " not found"));

        return books.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findByPublisher(String publisher, Pageable pageable) {
        Page<BookModel> books = bookRepository.findByPublisher(publisher, pageable)
                .orElseThrow(() -> new BookException("Publisher: " + publisher + " not found"));

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
                bookModel.getCreatedAt(),
                bookModel.getUpdatedAt()
        );
    }
}
