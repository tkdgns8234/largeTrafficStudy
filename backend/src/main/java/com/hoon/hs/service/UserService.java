package com.hoon.hs.service;

import com.hoon.hs.dto.SignUpUser;
import com.hoon.hs.entity.User;
import com.hoon.hs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor // final 필드 생성자 생성
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(SignUpUser signUpUser) {
        User user = new User();
        user.setUsername(signUpUser.getUsername());
        user.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        user.setEmail(signUpUser.getEmail());
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
