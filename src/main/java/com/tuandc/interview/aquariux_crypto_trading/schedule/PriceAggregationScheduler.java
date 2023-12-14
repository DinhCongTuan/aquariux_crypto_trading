package com.tuandc.interview.aquariux_crypto_trading.schedule;

import com.tuandc.interview.aquariux_crypto_trading.service.PriceAggregationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class PriceAggregationScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PriceAggregationScheduler.class);
    private final PriceAggregationService priceAggregationService;

    @Autowired
    public PriceAggregationScheduler(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void aggregatePricesAndStore() {
        try {
            long start = System.currentTimeMillis();
            logger.info("Start: store aggregated prices in the database");
            // Process and store aggregated prices in the database
            priceAggregationService.processAndStoreAggregatedPrices();
            long duration = System.currentTimeMillis() - start;
            logger.info("End: store aggregated prices in the database");
            logger.info("Time taken: {} ms", duration);
        } catch (Exception e) {
            // Handle exceptions or logging if necessary
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}

