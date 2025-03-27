package com.example.eightyage.domain.coupon.service;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.entity.CouponState;
import com.example.eightyage.domain.coupon.repository.CouponRepository;
import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.event.repository.EventRepository;
import com.example.eightyage.domain.event.service.EventService;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.ForbiddenException;
import com.example.eightyage.global.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final StringRedisTemplate stringRedisTemplate;

    public CouponResponseDto issueCoupon(AuthUser authUser, Long eventId) {
        // 수량 우선 차감
        Long remain = stringRedisTemplate.opsForValue().decrement("event:quantity:" + eventId);
        if (remain == null || remain < 0) { // atomic? `DESC`?
            throw new IllegalStateException("쿠폰 수량 소진");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!eventService.isValidEvent(event)) {
            throw new IllegalStateException("이벤트 기간이 아닙니다.");
        }

        if(couponRepository.existsByUserIdAndEventId(authUser.getUserId(), eventId)) {
            throw new IllegalStateException("이미 쿠폰 발급 받은 사용자입니다.");
        }

        // 쿠폰 발급 및 저장
        String couponCode = RandomCodeGenerator.generateCouponCode(10);
        Coupon coupon = new Coupon(couponCode, CouponState.VALID, User.fromAuthUser(authUser), event);

        couponRepository.save(coupon);

        return coupon.toDto();
    }

    public List<CouponResponseDto> getMyCoupons(AuthUser authUser) {
        List<Coupon> coupons = couponRepository.findAllByUserIdAndState(authUser.getUserId(), CouponState.VALID);

        return coupons.stream().map(coupon -> coupon.toDto()).collect(Collectors.toList());
    }

    public CouponResponseDto getCoupon(AuthUser authUser, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

        if(!coupon.getState().equals(CouponState.VALID)) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }

        if(!coupon.getUser().equals(User.fromAuthUser(authUser))) {
            throw new ForbiddenException("본인의 쿠폰이 아닙니다.");
        }

        return coupon.toDto();
    }
}
