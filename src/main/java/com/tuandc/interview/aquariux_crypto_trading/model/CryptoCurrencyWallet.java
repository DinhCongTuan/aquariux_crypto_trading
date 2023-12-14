package com.tuandc.interview.aquariux_crypto_trading.model;

import com.tuandc.interview.aquariux_crypto_trading.entity.CryptoCurrencyWalletEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyWallet {

    private Long walletId;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType; // BTC, ETH

    private BigDecimal balance;

    public CryptoCurrencyWallet(CryptoCurrencyWalletEntity entity) {
        this.walletId = entity.getWalletId();
        this.currencyType = entity.getCurrencyType();
        this.balance = entity.getBalance();
    }
}
