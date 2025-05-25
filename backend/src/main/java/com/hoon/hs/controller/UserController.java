package com.hoon.hs.controller;

import com.hoon.hs.entity.User;
import com.hoon.hs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getUserS() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> create(@RequestParam String userName, @RequestParam String password, @RequestParam String email) {
        User user = userService.createUser(userName, password, email);
        return ResponseEntity.ok(user); // entity 객체를 그대로 내보내는건 위험하지만 대용량 트래픽처리 focus 니까 그냥 진행하자.
    }

    // 보안상 spring security 와 엮어서 사용 (본인만 탈퇴 가능하도록 권한 제어)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id of the user delete", required = true) @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
