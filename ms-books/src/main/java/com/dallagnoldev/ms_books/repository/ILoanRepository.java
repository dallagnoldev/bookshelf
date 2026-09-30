package com.dallagnoldev.ms_books.repository;

import com.dallagnoldev.ms_books.model.LoanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ILoanRepository extends JpaRepository<LoanModel, UUID> {

    Page<LoanModel> findByUserEmail(String email, Pageable pageable);
}
