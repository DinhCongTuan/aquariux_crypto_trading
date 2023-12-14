package com.tuandc.interview.aquariux_crypto_trading.entity;

import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "crypto_wallet")
@NoArgsConstructor
@Data
public class CryptoCurrencyWalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType; // BTC, ETH, etc.

    private BigDecimal balance; // Use BigDecimal for precise calculations

    public CryptoCurrencyWalletEntity(CurrencyType currencyType, UserEntity user) {
        this.currencyType = currencyType;
        this.user = user;
        this.balance = BigDecimal.ZERO;
    }
}
