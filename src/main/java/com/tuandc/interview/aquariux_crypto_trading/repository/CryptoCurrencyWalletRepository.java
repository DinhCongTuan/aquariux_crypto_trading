package com.tuandc.interview.aquariux_crypto_trading.repository;

import com.tuandc.interview.aquariux_crypto_trading.entity.CryptoCurrencyWalletEntity;
import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCurrencyWalletRepository extends JpaRepository<CryptoCurrencyWalletEntity, Long> {

    CryptoCurrencyWalletEntity findByUserUserIdAndCurrencyType(Long userId, CurrencyType currencyType);
}
