package com.hoon.hs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoon.hs.entity.JwtBlacklist;

public interface JwtBlackListRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);

    Optional<JwtBlacklist> findByUsername(String username);
}