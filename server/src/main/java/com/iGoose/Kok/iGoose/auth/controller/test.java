package com.iGoose.Kok.iGoose.auth.controller;

import com.iGoose.Kok.common.jwt.JwtTokenProvider;
import com.iGoose.Kok.iGoose.auth.service.AuthService;
import com.iGoose.Kok.iGoose.auth.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class test {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TestService testService;

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String authentication) throws Exception {
        return testService.test(authentication);
    }
}


