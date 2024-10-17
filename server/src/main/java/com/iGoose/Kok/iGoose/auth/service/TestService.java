package com.iGoose.Kok.iGoose.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {


    public ResponseEntity<?> test(String authentication) throws Exception {

        return ResponseEntity.ok("Success with token: " + authentication);
    }

}
