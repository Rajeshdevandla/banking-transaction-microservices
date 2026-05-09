package com.rajesh.banking;

import com.rajesh.banking.transaction.dto.TransactionRequest;
import com.rajesh.banking.transaction.model.Transaction;
import com.rajesh.banking.transaction.repository.TransactionRepository;
import com.rajesh.banking.transaction.service.FraudDetectionService;
import com.rajesh.banking.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock TransactionRepository transactionRepository;
    @Mock KafkaTemplate kafkaTemplate;
    @Mock FraudDetectionService fraudDetectionService;
    @InjectMocks TransactionService transactionService;

    @Test
    void shouldInitiateValidTransaction() {
        when(fraudDetectionService.isSuspicious(any())).thenReturn(false);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionRequest req = new TransactionRequest();
        req.setFromAccount("ACC-001");
        req.setToAccount("ACC-002");
        req.setAmount(BigDecimal.valueOf(1000));
        req.setCurrency("USD");

        var result = transactionService.initiate(req);
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }
}
