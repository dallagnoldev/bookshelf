package com.dallagnoldev.ms_books.controller;

import com.dallagnoldev.ms_books.dto.LoanRequest;
import com.dallagnoldev.ms_books.dto.LoanResponse;
import com.dallagnoldev.ms_books.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody LoanRequest request, Authentication authentication) {

        String currentUserEmail = authentication.getName();

        LoanResponse loan = loanService.createLoan(request, currentUserEmail);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(loan.loanId())
                .toUri();

        return ResponseEntity.created(location).body(loan);
    }

    @GetMapping
    public ResponseEntity<Page<LoanResponse>> findAllLoans(
            @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication) {

            Page<LoanResponse> loans = loanService.findAllLoans(pageable, authentication);
            return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@loanSecurity.isOwner(authentication, #id)")
    public ResponseEntity<LoanResponse> findLoanById(@PathVariable UUID id) {
        LoanResponse loan = loanService.findLoanById(id);

        return ResponseEntity.ok(loan);
    }

    @PatchMapping("/renew/{id}")
    @PreAuthorize("@loanSecurity.isOwner(authentication, #id)")
    public ResponseEntity<Void> renewLoan(@PathVariable UUID id) {
        loanService.renewLoan(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/return/{id}")
    @PreAuthorize("@loanSecurity.isOwner(authentication, #id)")
    public ResponseEntity<Void> returnLoan(@PathVariable UUID id) {
        loanService.returnLoan(id);
        return ResponseEntity.noContent().build();
    }
}
