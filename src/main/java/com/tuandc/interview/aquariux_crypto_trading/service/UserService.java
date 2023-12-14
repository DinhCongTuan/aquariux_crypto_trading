package com.tuandc.interview.aquariux_crypto_trading.service;

import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import com.tuandc.interview.aquariux_crypto_trading.exception.BadParameterException;
import com.tuandc.interview.aquariux_crypto_trading.model.User;
import com.tuandc.interview.aquariux_crypto_trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private static final int INIT_WALLET_BALANCE = 5000;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        // Save the user to the database
        return convertToDTO(userRepository.save(newUser));
    }

    private User convertToDTO(UserEntity entity) {
        return new User(entity.getUserId(), entity.getUsername(), entity.getWalletBalance());
    }
}
