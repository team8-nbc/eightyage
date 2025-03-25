package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.dto.request.AuthSigninRequestDto;
import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_비밀번호_확인_불일치_실패() {
        // given
        AuthSignupRequestDto passwordCheckErrorSignupDto = new AuthSignupRequestDto("email@email.com", "nickname", "password1234", "password12341");

        // when & then
        assertThrows(BadRequestException.class,
                () -> authService.signup(passwordCheckErrorSignupDto),
                "비밀번호가 비밀번호 확인과 일치하지 않습니다.");
    }

    @Test
    void 회원가입_성공() {
        // given
        AuthSignupRequestDto successSignupDto = new AuthSignupRequestDto("email@email.com", "nickname", "password1234", "password1234");
        User user = new User(1L, successSignupDto.getEmail(), successSignupDto.getNickname(), UserRole.ROLE_USER);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(userService.saveUser(any(String.class), any(String.class), any(String.class))).willReturn(user);
        given(tokenService.createAccessToken(any(User.class))).willReturn(accessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(refreshToken);

        // when
        AuthTokensResponseDto result = authService.signup(successSignupDto);

        // then
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    void 로그인_삭제된_유저의_이메일일_경우_실패() {
        // given
        AuthSigninRequestDto seccessSigninDto = new AuthSigninRequestDto("email@email.com", "password1234");
        User user = new User(1L, seccessSigninDto.getEmail(), "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);

        // when & then
        assertThrows(UnauthorizedException.class,
                () -> authService.signin(seccessSigninDto),
                "탈퇴한 유저 이메일입니다.");
    }

    @Test
    void 로그인_비밀번호가_일치하지_않을_경우_실패() {
        // given
        AuthSigninRequestDto seccessSigninDto = new AuthSigninRequestDto("email@email.com", "password1234");
        User user = new User(1L, seccessSigninDto.getEmail(), "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "deletedAt", null);

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);
        given(passwordEncoder.matches(seccessSigninDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThrows(UnauthorizedException.class,
                () -> authService.signin(seccessSigninDto),
                "잘못된 비밀번호입니다.");
    }

    @Test
    void 로그인_성공() {
        // given
        AuthSigninRequestDto seccessSigninDto = new AuthSigninRequestDto("email@email.com", "password1234");
        User user = new User(1L, seccessSigninDto.getEmail(), "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "deletedAt", null);

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);
        given(passwordEncoder.matches(seccessSigninDto.getPassword(), user.getPassword())).willReturn(true);
        given(tokenService.createAccessToken(any(User.class))).willReturn(accessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(refreshToken);

        // when
        AuthTokensResponseDto result = authService.signin(seccessSigninDto);

        // then
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    void 토큰_재발급_성공() {
        // given
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        String refreshToken = "refreshToken";

        String reissuedAccessToken = "reissued-accessToken";
        String reissuedRefreshToken = "reissued-refreshToken";

        given(tokenService.reissueToken(refreshToken)).willReturn(user);
        given(tokenService.createAccessToken(any(User.class))).willReturn(reissuedAccessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(reissuedRefreshToken);

        // when
        AuthTokensResponseDto result = authService.reissueAccessToken(refreshToken);

        // then
        assertEquals(reissuedAccessToken, result.getAccessToken());
        assertEquals(reissuedRefreshToken, result.getRefreshToken());
    }
}
