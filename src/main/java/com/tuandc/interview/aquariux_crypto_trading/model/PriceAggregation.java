package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAggregation {

    private CurrencyType currencyType;

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private LocalDateTime timestamp;
}
