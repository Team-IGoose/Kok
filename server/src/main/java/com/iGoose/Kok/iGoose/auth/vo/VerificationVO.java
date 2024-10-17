package com.iGoose.Kok.iGoose.auth.vo;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerificationVO {

    private String uuid;

    private String method;

    private String code;

    private Date expire;
}
