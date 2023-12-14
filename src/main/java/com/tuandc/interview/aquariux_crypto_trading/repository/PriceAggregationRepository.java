package com.tuandc.interview.aquariux_crypto_trading.repository;

import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceAggregationRepository extends JpaRepository<PriceAggregationEntity, Long> {
    // Add custom methods for specific queries if needed
    Optional<PriceAggregationEntity> findByCurrencyType(CurrencyType currencyType);

    // Define methods to retrieve data from the database
    // Example: findLatestPrice
    @Query("SELECT pa FROM PriceAggregationEntity pa ORDER BY pa.timestamp DESC")
    PriceAggregationEntity findLatestPrice();
}
