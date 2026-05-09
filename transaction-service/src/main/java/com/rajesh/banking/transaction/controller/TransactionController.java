package com.rajesh.banking.transaction.controller;

import com.rajesh.banking.transaction.dto.TransactionRequest;
import com.rajesh.banking.transaction.dto.TransactionResponse;
import com.rajesh.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> initiateTransaction(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(transactionService.initiate(request));
    }

    @GetMapping("/{txnId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String txnId) {
        return ResponseEntity.ok(transactionService.getById(txnId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponse>> getAccountTransactions(
            @PathVariable String accountId, Pageable pageable) {
        return ResponseEntity.ok(transactionService.getByAccount(accountId, pageable));
    }
}
