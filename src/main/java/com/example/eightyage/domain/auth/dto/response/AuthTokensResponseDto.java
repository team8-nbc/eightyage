package com.example.eightyage.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokensResponseDto {

    private final String AccessToken;
    private final String refreshToken;

}
