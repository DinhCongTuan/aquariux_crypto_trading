package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.CryptoCurrencyWalletEntity;
import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.exception.NotFoundEntityException;
import com.tuandc.interview.aquariux_crypto_trading.model.CryptoCurrencyWallet;
import com.tuandc.interview.aquariux_crypto_trading.model.CurrencyType;
import com.tuandc.interview.aquariux_crypto_trading.model.Transaction;
import com.tuandc.interview.aquariux_crypto_trading.model.User;
import com.tuandc.interview.aquariux_crypto_trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final int INIT_WALLET_BALANCE = 5000;

    private final UserRepository userRepository;

    private final TradeService tradeService;

    @Autowired
    public UserService(UserRepository userRepository, TradeService tradeService) {
        this.userRepository = userRepository;
       this.tradeService = tradeService;
    }

    public User createUser(String username) {

        Optional<UserEntity> optional = userRepository.findByUsername(username);
        if (optional.isPresent()) {
            throw new BadParameterException("User name already exists");
        }

        // Create a new user with an initial wallet balance of 50,000 USDT
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setWalletBalance(new BigDecimal(INIT_WALLET_BALANCE)); // Set the initial balance
        // init crypto-currency-wallets
        newUser.setCryptoWallets(Arrays.asList(new CryptoCurrencyWalletEntity(CurrencyType.BTCUSDT, newUser),
                                                new CryptoCurrencyWalletEntity(CurrencyType.ETHUSDT, newUser)));
        // Save the user to the database
        return convertToDTO(userRepository.save(newUser));
    }

    public User getUserById(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("user not found"));
        return convertToDTO(entity);
    }

    public Page<Transaction> getUserTradingHistory(Long userId, int page, int size) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("user not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return tradeService.getUserTransactionHistory(user.getUserId(), pageable);

    }

    private User convertToDTO(UserEntity entity) {
        List<CryptoCurrencyWallet> cryptoCurrencyWallets = entity.getCryptoWallets()
                .stream().map(CryptoCurrencyWallet::new)
                .toList();
        return new User(entity.getUserId(),
                        entity.getUsername(),
                        entity.getWalletBalance(),
                        cryptoCurrencyWallets);
    }
}
