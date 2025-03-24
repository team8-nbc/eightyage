package com.example.eightyage.domain.auth.controller;

import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthAccessTokenResponseDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private static final int REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60; // 7주일

    /* 회원가입 */
    @PostMapping("/v1/auth/signup")
    public AuthAccessTokenResponseDto signup(
            @RequestBody AuthSignupRequestDto request,
            HttpServletResponse httpServletResponse
    ) {
        AuthTokensResponseDto tokensResponseDto = authService.signup(request);

        setRefreshTokenCookie(httpServletResponse, tokensResponseDto.getRefreshToken());

        return new AuthAccessTokenResponseDto(tokensResponseDto.getAccessToken());
    }

    /* http only 사용하기 위해 쿠키에 refreshToken 저장 */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(REFRESH_TOKEN_TIME);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }


}
