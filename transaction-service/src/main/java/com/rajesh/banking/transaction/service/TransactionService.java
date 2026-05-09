package com.rajesh.banking.transaction.service;

import com.rajesh.banking.transaction.dto.TransactionRequest;
import com.rajesh.banking.transaction.dto.TransactionResponse;
import com.rajesh.banking.transaction.event.TransactionEvent;
import com.rajesh.banking.transaction.model.Transaction;
import com.rajesh.banking.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final FraudDetectionService fraudDetectionService;

    @Transactional
    public TransactionResponse initiate(TransactionRequest request) {
        // Fraud check first
        if (fraudDetectionService.isSuspicious(request)) {
            throw new FraudDetectedException("Transaction flagged for review: " + request.getAmount());
        }

        Transaction txn = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .fromAccount(request.getFromAccount())
                .toAccount(request.getToAccount())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(Transaction.Status.PENDING)
                .createdAt(Instant.now())
                .build();

        Transaction saved = transactionRepository.save(txn);
        log.info("Transaction initiated: {} amount={}", saved.getId(), saved.getAmount());

        // Publish event to Kafka
        kafkaTemplate.send("transaction.initiated",
            TransactionEvent.of(saved));

        return TransactionResponse.from(saved);
    }

    public TransactionResponse getById(String txnId) {
        return transactionRepository.findById(txnId)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new TransactionNotFoundException(txnId));
    }
}
