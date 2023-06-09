package com.example.crud.repository;

import com.example.crud.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String name);

    void deleteByUsername(String name);
}
