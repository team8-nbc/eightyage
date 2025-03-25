package com.example.eightyage.domain.user.controller;

import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthAccessTokenResponseDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.user.dto.request.UserDeleteRequest;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.dto.AuthUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /* 회원탈퇴 */
    @PostMapping("/v1/users/delete")
    public void signup(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserDeleteRequest request
    ) {
        userService.deleteUser(authUser, request);
    }
}
