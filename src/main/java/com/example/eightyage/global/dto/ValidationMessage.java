package com.example.eightyage.global.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationMessage {

    public static final String NOT_BLANK_EMAIL = "이메일은 필수 입력 값입니다.";
    public static final String PATTERN_EMAIL = "이메일 형식으로 입력되어야 합니다.";
    public static final String NOT_BLANK_NICKNAME = "닉네임은 필수 입력 값입니다.";
    public static final String NOT_BLANK_PASSWORD = "비밀번호는 필수 입력 값입니다.";
    public static final String PATTERN_PASSWORD = "비밀번호는 영어, 숫자 포함 8자리 이상이어야 합니다.";
    public static final String PATTERN_PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

}