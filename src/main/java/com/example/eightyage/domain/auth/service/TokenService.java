package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.entity.RefreshToken;
import com.example.eightyage.domain.auth.repository.RefreshTokenRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.config.JwtUtil;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.eightyage.domain.auth.entity.TokenState.INVALIDATED;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /* Access Token 생성 */
    public String createAccessToken(User user) {
        return jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getUserRole());
    }

    /* Refresh Token 생성 */
    public String createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(user.getId()));
        return refreshToken.getRefreshToken();
    }

//    /* Refresh Token 만료 */
//    public void revokeRefreshToken(Long userId) {
//        RefreshToken refreshToken = findRefreshTokenById(userId);
//        refreshToken.updateTokenStatus(INVALIDATED);
//    }
//
//    /* Refresh Token 유효성 검사 */
//    public User reissueToken(String token) {
//
//        RefreshToken refreshToken = findByTokenOrElseThrow(token);
//
//        if (refreshToken.getTokenState() == INVALIDATED) {
//            throw new UnauthorizedException("사용이 만료된 refresh token 입니다.");
//        }
//        refreshToken.updateTokenStatus(INVALIDATED);
//
//        return userService.findUserByIdOrElseThrow(refreshToken.getUserId());
//    }
//
//    private RefreshToken findByTokenOrElseThrow(String token) {
//        return refreshTokenRepository.findByToken(token).orElseThrow(
//                () -> new NotFoundException("Not Found Token"));
//    }
//
//    private RefreshToken findRefreshTokenById(Long userId) {
//        return refreshTokenRepository.findById(userId).orElseThrow(
//                () -> new NotFoundException("Not Found Token"));
//    }
}
