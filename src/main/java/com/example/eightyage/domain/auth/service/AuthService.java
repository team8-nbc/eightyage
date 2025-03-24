package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthAccessTokenResponseDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    /* 회원가입 */
    public AuthTokensResponseDto signup(AuthSignupRequestDto request) {

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new BadRequestException("비밀번호 확인을 입력해주세요");
        }

        User user = userService.saveUser(request.getEmail(), request.getNickname(), request.getPassword());

        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.createRefreshToken(user);

        return new AuthTokensResponseDto(accessToken, refreshToken);
    }
}
