package com.example.eightyage.domain.coupon.controller;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.event.service.CouponService;
import com.example.eightyage.global.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/{eventId}")
    public ResponseEntity<CouponResponseDto> issueCoupon(@AuthenticationPrincipal AuthUser authUser, @PathVariable String eventId) {
        return ResponseEntity.ok(couponService.issueCoupon(authUser, eventId));
    }

}
