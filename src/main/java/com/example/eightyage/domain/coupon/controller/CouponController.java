package com.example.eightyage.domain.coupon.controller;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.service.CouponService;
import com.example.eightyage.global.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/{eventId}")
    public ResponseEntity<CouponResponseDto> issueCoupon(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long eventId) {
        return ResponseEntity.ok(couponService.issueCoupon(authUser, eventId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CouponResponseDto>> getMyCoupons(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(couponService.getMyCoupons(authUser));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> getCoupon(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCoupon(authUser, couponId));
    }
}
