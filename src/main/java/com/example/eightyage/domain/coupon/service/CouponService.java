package com.example.eightyage.domain.coupon.service;

import com.example.eightyage.domain.coupon.dto.request.CouponRequestDto;
import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.couponstate.CouponState;
import com.example.eightyage.domain.coupon.repository.CouponRepository;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    private static final String EVENT_QUANTITIY_PREFIX = "event:quantity:";
    private static final String EVENT_LOCK_PREFIX = "event:lock:";

    public CouponResponseDto saveCoupon(CouponRequestDto couponRequestDto) {
        Coupon coupon = new Coupon(
                couponRequestDto.getName(),
                couponRequestDto.getDescription(),
                couponRequestDto.getQuantity(),
                couponRequestDto.getStartDate(),
                couponRequestDto.getEndDate()
        );

        checkEventState(coupon);

        Coupon savedCoupon = couponRepository.save(coupon);

        stringRedisTemplate.opsForValue().set("event:quantity:" + savedCoupon.getId(), String.valueOf(savedCoupon.getQuantity()));

        return savedCoupon.toDto();
    }

    public Page<CouponResponseDto> getCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Coupon> events = couponRepository.findAll(pageable);

        // 모든 events들 checkState로 state 상태 갱신하기
        events.forEach(this::checkEventState);

        return events.map(Coupon::toDto);
    }

    public CouponResponseDto getCoupon(long eventId) {
        Coupon coupon = findByIdOrElseThrow(eventId);

        checkEventState(coupon);

        return coupon.toDto();
    }

    public CouponResponseDto updateCoupon(long eventId, CouponRequestDto couponRequestDto) {
        Coupon coupon = findByIdOrElseThrow(eventId);

        coupon.update(couponRequestDto);

        checkEventState(coupon);

        return coupon.toDto();
    }

    private void checkEventState(Coupon coupon) {
        CouponState prevState = coupon.getState();
        coupon.updateStateAt(LocalDateTime.now());

        if(coupon.getState() != prevState) {
            couponRepository.save(coupon);
        }
    }

    public Coupon getValidCouponOrThrow(Long eventId) {
        Coupon coupon = findByIdOrElseThrow(eventId);

        coupon.updateStateAt(LocalDateTime.now());

        if(coupon.getState() != CouponState.VALID) {
            throw new BadRequestException(ErrorMessage.INVALID_EVENT_PERIOD.getMessage());
        }

        return coupon;
    }

    public Coupon findByIdOrElseThrow(Long eventId) {
        return couponRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.EVENT_NOT_FOUND.getMessage()));
    }
}
