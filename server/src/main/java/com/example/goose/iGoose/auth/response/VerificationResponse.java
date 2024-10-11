package com.example.goose.iGoose.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponse {
    private String uuid;
    private String method;
    private Date expire;
    private String code;
}