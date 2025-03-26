package com.example.eightyage.domain.user.controller;

import com.example.eightyage.domain.user.dto.request.UserDeleteRequestDto;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.dto.AuthUser;
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
            @RequestBody UserDeleteRequestDto request
    ) {
        userService.deleteUser(authUser, request);
    }
}
