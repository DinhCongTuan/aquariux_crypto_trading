package com.tuandc.interview.aquariux_crypto_trading.entity;

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
@Table(name = "price_aggregation")
public class PriceAggregationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aggregationId;

//    @Enumerated(EnumType.STRING)
    private String currencyType; // ETHUSDT, BCTUSDT, etc.

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private LocalDateTime timestamp;

}

