package com.javabackend.northwind.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javabackend.northwind.core.security.models.RefreshToken;
import com.javabackend.northwind.core.security.models.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> deleteByUser(User user);

	

}
