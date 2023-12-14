package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.CryptoCurrencyWalletEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.TransactionEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.exception.NotFoundEntityException;
import com.tuandc.interview.aquariux_crypto_trading.model.TradeRequest;
import com.tuandc.interview.aquariux_crypto_trading.model.Transaction;
import com.tuandc.interview.aquariux_crypto_trading.model.TransactionType;
import com.tuandc.interview.aquariux_crypto_trading.repository.CryptoCurrencyWalletRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.PriceAggregationRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.TransactionRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private PriceAggregationRepository priceAggregationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoCurrencyWalletRepository walletRepository;

    @Transactional
    public void executeTrade(TradeRequest tradeRequest) {
        // validate request
        if (!isValidTradeRequest(tradeRequest)) {
            throw new BadParameterException("trade request parameters are not valid");
        }
        // get user info
        UserEntity userEntity = userRepository.findById(tradeRequest.getUserId())
                .orElseThrow(() -> new NotFoundEntityException("User not found"));

        // get unit price by currency pair and buy/sell
        PriceAggregationEntity priceAggregation = priceAggregationRepository
                .findByCurrencyType(tradeRequest.getCurrencyPair())
                .orElseThrow(() -> new NotFoundEntityException("Currency Pair not found"));

        BigDecimal totalPrice;
        BigDecimal unitPrice;
        CryptoCurrencyWalletEntity walletEntity =  walletRepository
                .findByUserUserIdAndCurrencyType(userEntity.getUserId(), tradeRequest.getCurrencyPair());
        if (TransactionType.BUY == tradeRequest.getTransactionType()) {
            // tinh totalPrice
            //
            unitPrice = priceAggregation.getAskPrice();
            totalPrice = unitPrice.multiply(tradeRequest.getAmount()); // amount * unit

            BigDecimal balanceUsd = userEntity.getWalletBalance();
            if (balanceUsd.compareTo(totalPrice) < 0) {
                throw new BadParameterException("Not enough USD in the wallet to perform the BUY transaction");
            }
            userEntity.setWalletBalance(balanceUsd.subtract(totalPrice)); // balance - price
            walletEntity.setBalance(walletEntity.getBalance().add(totalPrice)); // wallet balance + price
            userEntity.setCryptoWallets(List.of(walletEntity));

        } else { // SELL
            unitPrice = priceAggregation.getBidPrice();
            totalPrice = unitPrice.multiply(tradeRequest.getAmount());
            BigDecimal balance = userEntity.getWalletBalance();
            BigDecimal walletBalance = walletEntity.getBalance();
            if (walletBalance.compareTo(totalPrice) < 0) {
                throw new BadParameterException("Not enough COIN in the wallet to perform the SELL transaction");
            }
            userEntity.setWalletBalance(balance.add(totalPrice)); // balance + price
            walletEntity.setBalance(walletEntity.getBalance().subtract(totalPrice)); // wallet balance - price
        }
        userEntity.setCryptoWallets(List.of(walletEntity));
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(tradeRequest.getAmount());
        transactionEntity.setTransactionType(tradeRequest.getTransactionType());
        transactionEntity.setCurrencyType(tradeRequest.getCurrencyPair());
        transactionEntity.setAmount(tradeRequest.getAmount());
        transactionEntity.setUser(userEntity);
        transactionEntity.setTotalPrice(totalPrice);
        transactionEntity.setPricePerUnit(unitPrice);
        transactionEntity.setTimestamp(LocalDateTime.now());
        transactionEntity.setPriceAggregation(priceAggregation);
        transactionRepository.save(transactionEntity);
    }

    public Page<Transaction> getUserTransactionHistory(Long userId, Pageable pageable) {
        // Assuming you have a method to convert entities to DTOs
        // This assumes that the conversion logic exists
        return transactionRepository.findByUserUserId(userId, pageable)
                .map(this::convertEntityToDTO);
    }

    private Transaction convertEntityToDTO(TransactionEntity entity) {

        // Map entity fields to DTO fields
        Transaction dto = new Transaction();
        dto.setTransactionId(entity.getTransactionId());
        dto.setAmount(entity.getAmount());
        dto.setCurrencyType(entity.getCurrencyType());
        dto.setTransactionType(entity.getTransactionType());
        dto.setPricePerUnit(entity.getPricePerUnit());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setTimestamp(entity.getTimestamp());

        return dto;
    }

    private boolean isValidTradeRequest(TradeRequest tradeRequest) {
        // Add more validation rules here
        // For instance, check if the trade request parameters are valid
        return tradeRequest != null
                && tradeRequest.getUserId() != null
                && tradeRequest.getAmount() != null
                && tradeRequest.getCurrencyPair() != null
                && tradeRequest.getTransactionType() != null;
    }

}
