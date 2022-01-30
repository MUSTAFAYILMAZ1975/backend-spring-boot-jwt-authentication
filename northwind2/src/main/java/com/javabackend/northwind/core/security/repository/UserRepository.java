package com.javabackend.northwind.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javabackend.northwind.core.security.models.RefreshToken;
import com.javabackend.northwind.core.security.models.User;
import com.javabackend.northwind.core.security.payload.request.SignupRequest;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  
}