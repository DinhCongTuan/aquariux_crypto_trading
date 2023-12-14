package com.tuandc.interview.aquariux_crypto_trading.model;

import com.tuandc.interview.aquariux_crypto_trading.entity.TransactionEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
}
