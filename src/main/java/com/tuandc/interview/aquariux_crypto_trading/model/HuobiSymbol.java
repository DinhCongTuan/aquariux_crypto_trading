package com.tuandc.interview.aquariux_crypto_trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuobiSymbol {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("bid")
    private BigDecimal bidPrice;

    @JsonProperty("ask")
    private BigDecimal askPrice;

    public BigDecimal getBidPriceForSellOrder() {
        return bidPrice;
    }

    public BigDecimal getAskPriceForBuyOrder() {
        return askPrice;
    }
}
