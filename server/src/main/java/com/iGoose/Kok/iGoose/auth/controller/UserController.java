package com.iGoose.Kok.iGoose.auth.controller;

import com.iGoose.Kok.iGoose.auth.request.UserInfoRequest;
import com.iGoose.Kok.iGoose.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @PostMapping("/updateUserInfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoRequest request, Authentication authentication) throws Exception {
        String currentUsername = authentication.getName();
        userService.updateUserInfo(request, currentUsername);
        return ResponseEntity.ok("회원 정보 수정 성공");
    }
}
