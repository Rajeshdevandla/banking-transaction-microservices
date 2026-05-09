package com.rajesh.banking.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "transaction.initiated", groupId = "notification-group")
    public void onTransactionInitiated(TransactionEvent event) {
        log.info("Received transaction event: {}", event.getTransactionId());
        notificationService.sendConfirmation(event.getCustomerId(),
            String.format("Transaction %s initiated for $%.2f",
                event.getTransactionId(), event.getAmount()));
    }

    @KafkaListener(topics = "transaction.completed", groupId = "notification-group")
    public void onTransactionCompleted(TransactionEvent event) {
        notificationService.sendConfirmation(event.getCustomerId(),
            String.format("Transaction %s completed successfully", event.getTransactionId()));
    }

    @KafkaListener(topics = "transaction.failed", groupId = "notification-group")
    public void onTransactionFailed(TransactionEvent event) {
        notificationService.sendAlert(event.getCustomerId(),
            String.format("Transaction %s failed. Please contact support.", event.getTransactionId()));
    }
}
