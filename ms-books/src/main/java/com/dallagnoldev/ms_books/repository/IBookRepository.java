package com.dallagnoldev.ms_books.repository;

import com.dallagnoldev.ms_books.model.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBookRepository extends JpaRepository<BookModel, UUID> {
    boolean existsByIsbn(String isbn);

    Optional<BookModel> findByIsbn(String isbn);

    Optional<BookModel> findByTitle(String title);

    Optional<Page<BookModel>> findByAuthor(String author, Pageable pageable);

    Optional<Page<BookModel>> findByPublisher(String publisher, Pageable pageable);
}
