package com.tuandc.interview.aquariux_crypto_trading.comtroller;

import com.tuandc.interview.aquariux_crypto_trading.model.PriceAggregation;
import com.tuandc.interview.aquariux_crypto_trading.service.PriceAggregationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price-aggregate")
public class AggregatedPriceController extends BaseController {

    private final PriceAggregationService priceAggregationService;

    @Autowired
    public AggregatedPriceController(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

    @Operation(summary = "Get the latest aggregated prices for Ethereum and Bitcoin")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest prices")
    @GetMapping("/latestPrice")
    public List<PriceAggregation> getLatestAggregatedPrice() {
        return priceAggregationService.getLatestAggregatedPrices();
    }
}