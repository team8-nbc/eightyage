package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.dto.request.AuthSigninRequestDto;
import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthAccessTokenResponseDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /* 회원가입 */
    public AuthTokensResponseDto signup(AuthSignupRequestDto request) {

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new BadRequestException("비밀번호 확인을 입력해주세요");
        }

        User user = userService.saveUser(request.getEmail(), request.getNickname(), request.getPassword());

        return getTokenResponse(user);
    }

    /* 로그인 */
    @Transactional
    public AuthTokensResponseDto signin(AuthSigninRequestDto request) {
        User user = userService.findUserByEmailOrElseThrow(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("잘못된 비밀번호입니다.");
        }

        return getTokenResponse(user);
    }

    /* Access Token, Refresh Token 생성 및 저장 */
    private AuthTokensResponseDto getTokenResponse(User user) {

        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.createRefreshToken(user);

        return new AuthTokensResponseDto(accessToken, refreshToken);
    }
}
