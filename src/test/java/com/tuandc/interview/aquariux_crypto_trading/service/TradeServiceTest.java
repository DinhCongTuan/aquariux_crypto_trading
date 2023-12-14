package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.CryptoCurrencyWalletEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.PriceAggregationEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.TransactionEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.exception.NotFoundEntityException;
import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import com.tuandc.interview.aquariux_crypto_trading.model.TradeRequest;
import com.tuandc.interview.aquariux_crypto_trading.model.Transaction;
import com.tuandc.interview.aquariux_crypto_trading.model.TransactionType;
import com.tuandc.interview.aquariux_crypto_trading.repository.CryptoCurrencyWalletRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.PriceAggregationRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.TransactionRepository;
import com.tuandc.interview.aquariux_crypto_trading.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private PriceAggregationRepository priceAggregationRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptoCurrencyWalletRepository walletRepository;

    @Test
    public void executeTrade_BuyTransaction_Success() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("3");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWalletBalance(new BigDecimal("50000"));

        CryptoCurrencyWalletEntity walletEntity = new CryptoCurrencyWalletEntity();
        walletEntity.setUser(userEntity);
        walletEntity.setCurrencyType(CurrencyType.BTCUSDT);
        walletEntity.setBalance(BigDecimal.ZERO); // Initial balance is 0

        PriceAggregationEntity priceAggregationEntity = new PriceAggregationEntity();
        priceAggregationEntity.setCurrencyType(CurrencyType.BTCUSDT);
        priceAggregationEntity.setAskPrice(new BigDecimal("5000")); // Assume ask price for BTCUSDT is $5000

        // Mocking repository behavior
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        Mockito.when(walletRepository.findByUserUserIdAndCurrencyType(userId, CurrencyType.BTCUSDT))
                .thenReturn(walletEntity);
        Mockito.when(priceAggregationRepository.findByCurrencyType(CurrencyType.BTCUSDT))
                .thenReturn(Optional.of(priceAggregationEntity));

        // Creating the trade request
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserId(userId);
        tradeRequest.setAmount(amount);
        tradeRequest.setCurrencyPair(CurrencyType.BTCUSDT);
        tradeRequest.setTransactionType(TransactionType.BUY);

        // Executing the trade
        tradeService.executeTrade(tradeRequest);

        // Verifying the changes in user wallet and transactions
        BigDecimal expectedUserBalance = new BigDecimal("35000"); // 50000 - (3 * 5000) = 35000
        BigDecimal expectedWalletBalance = new BigDecimal("15000"); // Initial wallet balance is 0 + (3 * 5000) = 15000


        Mockito.verify(transactionRepository, Mockito.times(1))
                .save(Mockito.any(TransactionEntity.class));

        // Assertions
        Assert.assertEquals(expectedUserBalance, userEntity.getWalletBalance());
        Assert.assertEquals(expectedWalletBalance, walletEntity.getBalance());
    }

    @Test
    public void executeTrade_SellTransaction_Success() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("10");
        CurrencyType currencyPair = CurrencyType.BTCUSDT;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWalletBalance(new BigDecimal("50000"));

        CryptoCurrencyWalletEntity walletEntity = new CryptoCurrencyWalletEntity();
        walletEntity.setUser(userEntity);
        walletEntity.setCurrencyType(currencyPair);
        walletEntity.setBalance(new BigDecimal("50000")); // Initial balance is 50,000 USDT

        PriceAggregationEntity priceAggregationEntity = new PriceAggregationEntity();
        priceAggregationEntity.setCurrencyType(currencyPair);
        priceAggregationEntity.setBidPrice(new BigDecimal("5000")); // Assume bid price for BTCUSDT is $5000

        // Mocking repository behavior
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        Mockito.when(walletRepository.findByUserUserIdAndCurrencyType(userId, currencyPair)).thenReturn(walletEntity);
        Mockito.when(priceAggregationRepository.findByCurrencyType(currencyPair)).thenReturn(Optional.of(priceAggregationEntity));

        // Creating the trade request
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserId(userId);
        tradeRequest.setAmount(amount);
        tradeRequest.setCurrencyPair(currencyPair);
        tradeRequest.setTransactionType(TransactionType.SELL);

        // Executing the trade
        tradeService.executeTrade(tradeRequest);

        // Verifying the changes in user wallet and transactions
        BigDecimal expectedUserBalance = new BigDecimal("100000"); // 50000 + (10 * 5000) = 100000
        BigDecimal expectedWalletBalance = new BigDecimal("0"); // Initial wallet balance is 50000 - (10 * 5000) = 0

        Mockito.verify(transactionRepository, Mockito.times(1))
                .save(Mockito.any(TransactionEntity.class));

        // Assertions
        Assert.assertEquals(expectedUserBalance, userEntity.getWalletBalance());
        Assert.assertEquals(expectedWalletBalance, walletEntity.getBalance());
    }

    @Test(expected = NotFoundEntityException.class)
    public void executeTrade_UserNotFound_ThrowException() {
        // Implement test scenario when the user is not found
        // Mocking the required entities and their behaviors
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("10");

        // Mocking repository behavior when user is not found
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Creating the trade request
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserId(userId);
        tradeRequest.setAmount(amount);
        tradeRequest.setCurrencyPair(CurrencyType.BTCUSDT);
        tradeRequest.setTransactionType(TransactionType.SELL);

        // Executing the trade
        tradeService.executeTrade(tradeRequest); // This should throw NotFoundEntityException
    }

    @Test(expected = BadParameterException.class)
    public void executeTrade_InsufficientFunds_ThrowException() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100000"); // Assuming the user has insufficient funds

        // Mocking repository behavior when user is found
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWalletBalance(new BigDecimal("100")); // Setting a low balance

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        PriceAggregationEntity priceAggregationEntity = new PriceAggregationEntity();
        priceAggregationEntity.setCurrencyType(CurrencyType.BTCUSDT);
        priceAggregationEntity.setAskPrice(new BigDecimal("5000"));
        Mockito.when(priceAggregationRepository.findByCurrencyType(CurrencyType.BTCUSDT))
                .thenReturn(Optional.of(priceAggregationEntity));
        // Creating the trade request
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserId(userId);
        tradeRequest.setAmount(amount);
        tradeRequest.setCurrencyPair(CurrencyType.BTCUSDT);
        tradeRequest.setTransactionType(TransactionType.BUY);

        // Executing the trade
        tradeService.executeTrade(tradeRequest);
    }

    @Test
    public void getUserTransactionHistory_ValidUserId_ReturnPageOfTransactions() {
        Long userId = 1L;

        // Mocking the transaction entities for the user
        TransactionEntity transaction1 = new TransactionEntity();
        transaction1.setTransactionId(1L);
        transaction1.setUser(new UserEntity()); // Assuming no wallet balance
        transaction1.setAmount(BigDecimal.valueOf(10));
        transaction1.setCurrencyType(CurrencyType.BTCUSDT);
        transaction1.setTransactionType(TransactionType.BUY);
        transaction1.setPricePerUnit(BigDecimal.valueOf(50000));
        transaction1.setTotalPrice(BigDecimal.valueOf(500000));
        transaction1.setTimestamp(LocalDateTime.now());

        TransactionEntity transaction2 = new TransactionEntity();
        transaction2.setTransactionId(2L);
        transaction2.setUser(new UserEntity()); // Assuming no wallet balance
        transaction2.setAmount(BigDecimal.valueOf(5));
        transaction2.setCurrencyType(CurrencyType.ETHUSDT);
        transaction2.setTransactionType(TransactionType.SELL);
        transaction2.setPricePerUnit(BigDecimal.valueOf(3000));
        transaction2.setTotalPrice(BigDecimal.valueOf(15000));
        transaction2.setTimestamp(LocalDateTime.now());

        List<TransactionEntity> transactions = Arrays.asList(transaction1, transaction2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("timestamp").descending());
        Page<TransactionEntity> transactionPage = new PageImpl<>(transactions, pageable, transactions.size());

        Mockito.when(transactionRepository.findByUserUserId(userId, pageable)).thenReturn(transactionPage);

        // Fetching user transaction history
        Page<Transaction> userTransactionHistory = tradeService.getUserTransactionHistory(userId, pageable);

        // Assertions
        assertEquals(2, userTransactionHistory.getContent().size());
    }

}

