package com.tuandc.interview.aquariux_crypto_trading.repository;

import com.tuandc.interview.aquariux_crypto_trading.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
