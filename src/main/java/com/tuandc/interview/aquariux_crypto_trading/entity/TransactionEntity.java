package com.tuandc.interview.aquariux_crypto_trading.entity;

import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import com.tuandc.interview.aquariux_crypto_trading.model.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType; // BTC, ETH, etc.

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // BUY or SELL

    private BigDecimal amount;

    private BigDecimal pricePerUnit;

    private BigDecimal totalPrice;

    private LocalDateTime timestamp;
}
