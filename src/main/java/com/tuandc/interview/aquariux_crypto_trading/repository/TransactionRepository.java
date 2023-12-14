package com.tuandc.interview.aquariux_crypto_trading.repository;

import com.tuandc.interview.aquariux_crypto_trading.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Page<TransactionEntity> findByUserUserId(Long userId, Pageable pageable);

}
