package com.rajesh.banking.transaction.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions",
    indexes = {
        @Index(name = "idx_from_account", columnList = "fromAccount"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_status", columnList = "status")
    })
@Data
@Builder
public class Transaction {

    @Id
    private String id;

    @Column(nullable = false)
    private String fromAccount;

    @Column(nullable = false)
    private String toAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Instant createdAt;
    private Instant completedAt;

    public enum Status { PENDING, PROCESSING, COMPLETED, FAILED, REVERSED }
}
