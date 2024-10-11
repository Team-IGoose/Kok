package com.example.goose.iGoose.auth.vo;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements UserDetails {


    private String uuid;

    private String id;

    private String name;

    private String password;

    private String pronoun;

    private String intor;

    private String gender;

    private String status;

    private String profile;

    private String feeling;

    private Boolean is_professional;

    private String revenue_status;

    private LocalDateTime created;

    private LocalDateTime updated;

    private String email;

    private String phone_number;

    private Boolean email_verified;

    private Boolean phone_verified;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }
}
