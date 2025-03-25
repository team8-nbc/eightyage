package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.entity.RefreshToken;
import com.example.eightyage.domain.auth.repository.RefreshTokenRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import com.example.eightyage.global.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.eightyage.domain.auth.entity.TokenState.INVALIDATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenService tokenService;

    /* createAccessToken */
    @Test
    void 토큰발급_AccessToken_발급_성공() {
        // given
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        String accessToken = "accessToken";

        given(jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getUserRole())).willReturn(accessToken);

        // when
        String result = tokenService.createAccessToken(user);

        // then
        assertEquals(accessToken, result);
    }

    /* createRefreshToken */
    @Test
    void 토큰발급_RefreshToken_발급_성공() {
        // given
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        RefreshToken mockRefreshToken = new RefreshToken(user.getId());

        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(mockRefreshToken);

        // when
        String createdRefreshToken = tokenService.createRefreshToken(user);

        // then
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        assertEquals(mockRefreshToken.getToken(), createdRefreshToken);
    }

    /* reissueToken */
    @Test
    void 토큰유효성검사_비활성_상태일때_실패() {
        // given
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        String refreshToken = "refresh-token";

        RefreshToken mockRefreshToken = mock(RefreshToken.class);

        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.of(mockRefreshToken));
        given(mockRefreshToken.getTokenState()).willReturn(INVALIDATED);

        // when & then
        assertThrows(UnauthorizedException.class,
                () -> tokenService.reissueToken(refreshToken),
                "사용이 만료된 refresh token 입니다.");
    }

    @Test
    void 토큰검색_토큰이_없을_시_실패() {
        //given
        String refreshToken = "refresh-token";

        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class,
                () -> tokenService.reissueToken(refreshToken),
                "리프레시 토큰을 찾을 수 없습니다.");
    }

    @Test
    void 토큰유효성검사_성공() {
        // given
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        String refreshToken = "refresh-token";

        RefreshToken mockRefreshToken = mock(RefreshToken.class);

        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.of(mockRefreshToken));
        given(userService.findUserByIdOrElseThrow(mockRefreshToken.getUserId())).willReturn(user);

        // when
        User result = tokenService.reissueToken(refreshToken);

        // then
        assertNotNull(result);
        verify(mockRefreshToken, times(1)).updateTokenStatus(INVALIDATED);
    }
}
