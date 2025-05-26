package com.hoon.hs.service;

import com.hoon.hs.entity.JwtBlacklist;
import com.hoon.hs.jwt.JwtUtil;
import com.hoon.hs.repository.JwtBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtBlacklistService {

    private final JwtBlackListRepository jwtBlacklistRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtBlacklistService(JwtBlackListRepository jwtBlacklistRepository, JwtUtil jwtUtil) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    public void blacklistToken(String token, LocalDateTime expirationTime, String username) {
        Optional<JwtBlacklist> existingBlacklist = jwtBlacklistRepository.findByUsername(username);
        JwtBlacklist jwtBlacklist;

        if (existingBlacklist.isPresent()) {
            jwtBlacklist = existingBlacklist.get();
            jwtBlacklist.setToken(token); // 기존 토큰 업데이트
            jwtBlacklist.setExpirationTime(expirationTime); // 만료 시간 업데이트
        } else {
            jwtBlacklist = new JwtBlacklist();
            jwtBlacklist.setToken(token);
            jwtBlacklist.setExpirationTime(expirationTime);
            jwtBlacklist.setUsername(username);
        }

        jwtBlacklistRepository.save(jwtBlacklist);
    }

    public boolean isTokenBlacklisted(String currentToken) {
        String username = jwtUtil.getUsernameFromToken(currentToken);
        Optional<JwtBlacklist> blacklistedToken = jwtBlacklistRepository.findByUsername(username);
        if (blacklistedToken.isEmpty()) {
            return false;
        }

        LocalDateTime token = jwtUtil.getExpirationDateFromToken(currentToken);
        return blacklistedToken.get().getExpirationTime().isAfter(token);
    }
}