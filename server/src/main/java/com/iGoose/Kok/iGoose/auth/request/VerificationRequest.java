package com.iGoose.Kok.iGoose.auth.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest {
    private String uuid;
    private String method;
    private String code;
    private Date expire;
    private String type;
}
