package com.dallagnoldev.ms_books.service;

import com.dallagnoldev.ms_books.dto.LoanRequest;
import com.dallagnoldev.ms_books.dto.LoanResponse;
import com.dallagnoldev.ms_books.enums.LoanStatus;
import com.dallagnoldev.ms_books.exception.BookException;
import com.dallagnoldev.ms_books.exception.LoanException;
import com.dallagnoldev.ms_books.exception.NotFoundException;
import com.dallagnoldev.ms_books.model.BookModel;
import com.dallagnoldev.ms_books.model.LoanModel;
import com.dallagnoldev.ms_books.producer.LoanProducer;
import com.dallagnoldev.ms_books.repository.IBookRepository;
import com.dallagnoldev.ms_books.repository.ILoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final ILoanRepository loanRepository;
    private final IBookRepository bookRepository;
    private final BookService bookService;
    private final LoanProducer loanProducer;

    @CacheEvict(value = { "books", "paginatedBooks" }, allEntries = true)
    @Transactional
    public LoanResponse createLoan(LoanRequest request, String email) {

        BookModel book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BookException("Book not found"));

        if (!book.isAvailable()) {
            throw new BookException("Book is not available");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        LocalDate returnLoanDate = LocalDate.now().plusDays(15);

        LoanModel loanModel = LoanModel.builder()
                .userEmail(email)
                .book(book)
                .loanDate(LocalDate.now())
                .returnLoanDate(returnLoanDate)
                .loanStatus(LoanStatus.ACTIVE)
                .build();

        LoanResponse response = toResponse(loanRepository.save(loanModel));
        loanProducer.receiptEmail(response);

        return response;
    }

    @Transactional(readOnly = true)
    public LoanResponse findLoanById(UUID loanId) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Loan not found"));

        return toResponse(loan);
    }

    @Transactional(readOnly = true)
    public Page<LoanResponse> findAllLoans(Pageable pageable, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return loanRepository.findAll(pageable).map(this::toResponse);
        } else {
            return loanRepository.findByUserEmail(authentication.getName(), pageable).map(this::toResponse);
        }
    }

    @Transactional
    public void renewLoan(UUID loanId) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan with id: " + loanId + " not found"));

        if (loan.getLoanStatus() == LoanStatus.RETURNED) {
            throw new LoanException("Cannot renew a returned loan");
        }

        if (loan.getReturnLoanDate().isBefore(LocalDate.now())) {
            throw new LoanException("You can no longer renew this loan. Return date overdue");
        }

        LocalDate newReturnLoanDate = loan.getReturnLoanDate().plusDays(15);

        loan.setReturnLoanDate(newReturnLoanDate);
        loanRepository.save(loan);
    }

    @CacheEvict(value = { "books", "paginatedBooks" }, allEntries = true)
    @Transactional
    public void returnLoan(UUID loanId) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with Id: " + loanId));

        if (loan.getLoanStatus() == LoanStatus.RETURNED) {
            throw new LoanException("Loan is already returned");
        }

        loan.setLoanStatus(LoanStatus.RETURNED);

        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);

        loanRepository.save(loan);

    }

    public LoanResponse toResponse(LoanModel loanModel) {
        return new LoanResponse(
                loanModel.getLoanId(),
                loanModel.getUserEmail(),
                bookService.toResponse(loanModel.getBook()),
                loanModel.getLoanDate(),
                loanModel.getReturnLoanDate(),
                loanModel.getLoanStatus(),
                loanModel.getCreatedAt()
        );
    }
}
