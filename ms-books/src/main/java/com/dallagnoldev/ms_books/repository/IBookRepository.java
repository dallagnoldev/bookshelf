package com.dallagnoldev.ms_books.repository;

import com.dallagnoldev.ms_books.model.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface IBookRepository extends JpaRepository<BookModel, UUID> {
    boolean existsByIsbn(String isbn);

    @Query("select b from BookModel b where " +
            "(:title is null or lower(b.title) like lower(cast(:title as string))) and " +
            "(:author is null or lower(b.author) like lower(cast(:author as string))) and " +
            "(:publisher is null or lower(b.publisher) like lower(cast(:publisher as string))) and " +
            "(:isbn is null or b.isbn = :isbn) and " +
            "(:category is null or lower(b.category) like lower(cast(:category as string)))"
    )
    Page<BookModel> findByFilters(
            @Param("title") String title,
            @Param("author") String author,
            @Param("publisher") String publisher,
            @Param("isbn") String isbn,
            @Param("category") String category,
            Pageable pageable
    );
}
