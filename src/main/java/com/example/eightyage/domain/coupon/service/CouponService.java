package com.example.eightyage.domain.coupon.service;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.entity.CouponState;
import com.example.eightyage.domain.coupon.repository.CouponRepository;
import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.event.service.EventService;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.ErrorMessage;
import com.example.eightyage.global.exception.ForbiddenException;
import com.example.eightyage.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final EventService eventService;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String EVENT_QUANTITIY_PREFIX = "event:quantity:";
    private static final String EVENT_LOCK_PREFIX = "event:lock:";
    private final RedissonClient redissonClient;

    public CouponResponseDto issueCoupon(AuthUser authUser, Long eventId) {

        RLock rLock = redissonClient.getLock(EVENT_LOCK_PREFIX + eventId);
        boolean isLocked = false;

        try {
            isLocked = rLock.tryLock(3, 10, TimeUnit.SECONDS);  // 3초 안에 락을 획득, 10초 뒤에는 자동 해제

            if (!isLocked) {
                throw new BadRequestException(ErrorMessage.CAN_NOT_ACCESS.getMessage()); // 락 획득 실패
            }

            Event event = eventService.getValidEventOrThrow(eventId);

            if (couponRepository.existsByUserIdAndEventId(authUser.getUserId(), eventId)) {
                throw new BadRequestException(ErrorMessage.COUPON_ALREADY_ISSUED.getMessage());
            }

            Long remain = Long.parseLong(stringRedisTemplate.opsForValue().get(EVENT_QUANTITIY_PREFIX + eventId));
            if (remain == 0 || remain < 0) {
                throw new BadRequestException(ErrorMessage.COUPON_OUT_OF_STOCK.getMessage());
            }
            stringRedisTemplate.opsForValue().decrement(EVENT_QUANTITIY_PREFIX + eventId);

            // 쿠폰 발급 및 저장
            Coupon coupon = Coupon.create(User.fromAuthUser(authUser),event);
            couponRepository.save(coupon);

            return coupon.toDto();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BadRequestException(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
        } finally {
            if (isLocked) {
                rLock.unlock();
            }
        }
    }

    public Page<CouponResponseDto> getMyCoupons(AuthUser authUser, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Coupon> coupons = couponRepository.findAllByUserIdAndState(authUser.getUserId(), CouponState.VALID, pageable);

        return coupons.map(Coupon::toDto);
    }

    public CouponResponseDto getCoupon(AuthUser authUser, Long couponId) {
        Coupon coupon = findByIdOrElseThrow(couponId);

        if(!coupon.getUser().equals(User.fromAuthUser(authUser))) {
            throw new ForbiddenException(ErrorMessage.COUPON_FORBIDDEN.getMessage());
        }

        if(!coupon.getState().equals(CouponState.VALID)) {
            throw new BadRequestException(ErrorMessage.COUPON_ALREADY_USED.getMessage());
        }

        return coupon.toDto();
    }

    public Coupon findByIdOrElseThrow(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.COUPON_NOT_FOUND.getMessage()));
    }
}
