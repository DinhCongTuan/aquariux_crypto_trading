package com.tuandc.interview.aquariux_crypto_trading.repository;

import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceAggregationRepository extends JpaRepository<PriceAggregationEntity, Long> {
    // Add custom methods for specific queries if needed
    Optional<PriceAggregationEntity> findByCurrencyType(String currencyType);
}
