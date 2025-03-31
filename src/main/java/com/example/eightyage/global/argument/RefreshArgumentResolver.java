package com.example.eightyage.global.argument;

import com.example.eightyage.global.annotation.RefreshToken;
import com.example.eightyage.global.exception.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.example.eightyage.global.exception.ErrorMessage.REFRESH_TOKEN_MUST_BE_STRING;
import static com.example.eightyage.global.exception.ErrorMessage.REFRESH_TOKEN_NOT_FOUND;

public class RefreshArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasRefreshTokenAnnotation = parameter.getParameterAnnotation(RefreshToken.class) != null;
        boolean isStringType = parameter.getParameterType().equals(String.class);

        if (hasRefreshTokenAnnotation != isStringType) {
            throw new UnauthorizedException(REFRESH_TOKEN_MUST_BE_STRING.getMessage());
        }
        return hasRefreshTokenAnnotation;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new UnauthorizedException(REFRESH_TOKEN_NOT_FOUND.getMessage());
    }
}
