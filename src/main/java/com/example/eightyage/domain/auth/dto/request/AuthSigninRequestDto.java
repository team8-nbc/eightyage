package com.example.eightyage.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthSigninRequestDto {

    private String email;
    private String password;

}
