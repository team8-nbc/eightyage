package com.example.eightyage.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthAccessTokenResponseDto {

    private final String accessToken;

}