package com.tuandc.interview.aquariux_crypto_trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BinanceSymbol {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("bidPrice")
    private BigDecimal bidPrice;

    @JsonProperty("askPrice")
    private BigDecimal askPrice;

    public BigDecimal getBidPriceForSellOrder() {
        return bidPrice;
    }

    public BigDecimal getAskPriceForBuyOrder() {
        return askPrice;
    }
}
