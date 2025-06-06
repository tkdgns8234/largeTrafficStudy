package com.hoon.hs.controller;

import com.hoon.hs.dto.SignUpUser;
import com.hoon.hs.entity.User;
import com.hoon.hs.jwt.JwtUtil;
import com.hoon.hs.service.CustomUserDetailsService;
import com.hoon.hs.service.JwtBlacklistService;
import com.hoon.hs.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    @GetMapping("")
    public ResponseEntity<List<User>> getUserS() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> createUser(@RequestBody SignUpUser signUpUser) {
        User user = userService.createUser(signUpUser);
        return ResponseEntity.ok(user);
    }

    // 보안상 spring security 와 엮어서 사용 (본인만 탈퇴 가능하도록 권한 제어)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id of the user delete", required = true) @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String token = jwtUtil.generateToken(userDetails.getUsername());
        Cookie cookie = new Cookie("hs_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        return token;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("hs_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(cookie);
    }

    @PostMapping("/logout/all")
    public void logout(@RequestParam(required = false) String requestToken, @CookieValue(value = "hs_token", required = false) String cookieToken, HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        String headerToken = request.getHeader("Authorization");
        if (requestToken != null) {
            token = requestToken;
        } else if (cookieToken != null) {
            token = cookieToken;
        } else if (headerToken != null && headerToken.startsWith("Bearer ")) {
            token = headerToken.substring(7);
        }

        String username = jwtUtil.getUsernameFromToken(token);
        LocalDateTime expirationTime = jwtUtil.getExpirationDateFromToken(token);
        jwtBlacklistService.blacklistToken(token, expirationTime, username);
        Cookie cookie = new Cookie("onion_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(cookie);
    }

    @PostMapping("/token/validation")
    @ResponseStatus(HttpStatus.OK)
    public void jwtValidate(@RequestParam String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is not validation");
        }
    }
}
