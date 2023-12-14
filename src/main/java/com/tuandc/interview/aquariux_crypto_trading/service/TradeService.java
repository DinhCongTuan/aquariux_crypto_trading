package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.TransactionEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.exception.NotFoundEntityException;
import com.tuandc.interview.aquariux_crypto_trading.model.TradeRequest;
import com.tuandc.interview.aquariux_crypto_trading.model.TransactionType;
import com.tuandc.interview.aquariux_crypto_trading.repository.PriceAggregationRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.TransactionRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TradeService {

    @Autowired
    private PriceAggregationRepository priceAggregationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

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
        if (TransactionType.BUY == tradeRequest.getTransactionType()) {
            unitPrice = priceAggregation.getAskPrice();
            totalPrice = unitPrice.multiply(tradeRequest.getAmount()); // amount * unit
            BigDecimal balance = userEntity.getWalletBalance();
            userEntity.setWalletBalance(balance.subtract(totalPrice)); // balance - price
        } else { // SELL
            unitPrice = priceAggregation.getBidPrice();
            totalPrice = unitPrice.multiply(tradeRequest.getAmount());
            BigDecimal balance = userEntity.getWalletBalance();
            userEntity.setWalletBalance(balance.add(totalPrice)); // balance + price
        }
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(tradeRequest.getAmount());
        transactionEntity.setTransactionType(tradeRequest.getTransactionType());
        transactionEntity.setCurrencyType(tradeRequest.getCurrencyPair());
        transactionEntity.setAmount(tradeRequest.getAmount());
        transactionEntity.setUser(userEntity);
        transactionEntity.setTotalPrice(totalPrice);
        transactionEntity.setPricePerUnit(unitPrice);

        transactionRepository.save(transactionEntity);
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
