package com.example.goose.iGoose.auth.service;

import com.example.goose.iGoose.auth.request.UserInfoRequest;
import com.example.goose.iGoose.auth.mapper.AuthMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder

public class UserService {

    private final AuthMapper authMapper;

    public void updateUserInfo(UserInfoRequest request, String uuid) throws Exception {
        authMapper.updateUserInfo(request, uuid);
    }
}
