package com.iGoose.Kok.iGoose.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationUserInfoResponse {
    private String message;
    private String user_name;
    private String profile;
}
