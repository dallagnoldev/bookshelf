package com.dallagnoldev.ms_books.repository;

import com.dallagnoldev.ms_books.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IBookRepository extends JpaRepository<BookModel, UUID> {
}
