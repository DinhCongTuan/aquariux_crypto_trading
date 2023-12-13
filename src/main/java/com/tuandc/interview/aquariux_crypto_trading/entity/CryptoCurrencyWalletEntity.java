package com.tuandc.interview.aquariux_crypto_trading.entity;

import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "crypto_wallet")
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

}