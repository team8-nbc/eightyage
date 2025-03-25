package com.example.eightyage.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthSigninRequestDto {

    private String email;
    private String password;

}
