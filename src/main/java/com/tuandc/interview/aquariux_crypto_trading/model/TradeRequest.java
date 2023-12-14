package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TradeRequest {

    private Long userId;

    private CurrencyType currencyPair;

    private BigDecimal amount; //The quantity of cryptocurrency involved in the transaction.

    private TransactionType transactionType; // Enum: BUY, SELL
}
