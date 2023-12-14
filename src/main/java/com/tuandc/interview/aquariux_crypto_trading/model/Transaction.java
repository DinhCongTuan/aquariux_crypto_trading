package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private Long transactionId;
    private Long userId;
    private CurrencyType currencyType;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;
    private LocalDateTime timestamp;
}
