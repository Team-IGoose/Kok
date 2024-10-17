package com.iGoose.Kok.iGoose.auth.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
    private String user_name;

    private String pronoun;

    private String intro;

    private String gender;

    private String status;

    private String profile;

    private String feeling;

    private Boolean is_professional;

    private String revenue_status;

    private LocalDateTime updated;
}
