package com.tuandc.interview.aquariux_crypto_trading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private Long userId;

    private String username;

    private BigDecimal walletBalance;

    private List<CryptoCurrencyWallet> cryptoCurrencyWallets;
}
