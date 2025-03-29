package com.example.eightyage.domain.coupon.controller;

import com.example.eightyage.domain.coupon.dto.request.CouponRequestDto;
import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.example.eightyage.domain.coupon.service.CouponService;
import com.example.eightyage.global.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/v1/events")
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(couponService.saveCoupon(couponRequestDto));
    }

    @GetMapping("/v1/events")
    public ResponseEntity<Page<CouponResponseDto>> getCoupons(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(couponService.getCoupons(page, size));
    }

    @GetMapping("/v1/events/{eventId}")
    public ResponseEntity<CouponResponseDto> getCoupon(@PathVariable long eventId) {
        return ResponseEntity.ok(couponService.getCoupon(eventId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/v1/events/{eventId}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable long eventId, @Valid @RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(couponService.updateCoupon(eventId, couponRequestDto));
    }
}
