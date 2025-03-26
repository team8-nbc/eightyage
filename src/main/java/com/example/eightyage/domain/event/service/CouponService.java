package com.example.eightyage.domain.event.service;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.repository.CouponRepository;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RandomCodeGenerator randomCodeGenerator;

    public CouponResponseDto issueCoupon(AuthUser authUser, String eventId) {
        // 해당 이벤트 조회

        // 이벤트 상태 및 수량 확인

        // 사용자 발급 여부 확인

        // 랜덤 쿠폰 번호 생성
        String couponCode = randomCodeGenerator.generateCouponCode(10);
        // 쿠폰 발급 (DB 저장)

        // 응답: 발급된 쿠폰 정보
        return new CouponResponseDto();
    }
}
