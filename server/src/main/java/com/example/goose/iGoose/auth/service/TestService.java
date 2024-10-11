package com.example.goose.iGoose.auth.service;


import com.example.goose.common.jwt.JwtTokenProvider;
import com.google.api.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static java.rmi.server.LogStream.log;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {


    public ResponseEntity<?> test(String authentication) throws Exception {

        return ResponseEntity.ok("Success with token: " + authentication);
    }

}
