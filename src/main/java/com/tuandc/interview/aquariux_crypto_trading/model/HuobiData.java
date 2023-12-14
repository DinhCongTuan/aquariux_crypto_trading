package com.tuandc.interview.aquariux_crypto_trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HuobiData {

    @JsonProperty("data")
    private List<HuobiSymbol> data;
}
