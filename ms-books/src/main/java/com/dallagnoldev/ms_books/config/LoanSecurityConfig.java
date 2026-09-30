package com.dallagnoldev.ms_books.config;

import com.dallagnoldev.ms_books.repository.ILoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("loanSecurity")
@RequiredArgsConstructor
public class LoanSecurityConfig {

    private final ILoanRepository loanRepository;

    public boolean isOwner(Authentication authentication, UUID loanId) {
        if (authentication == null || loanId == null) {
            return false;
        }

        String currentUserEmail = authentication.getName();

        return loanRepository.findById(loanId)
                .map(loan -> loan.getUserEmail().equalsIgnoreCase(currentUserEmail))
                .orElse(false);
    }
}
