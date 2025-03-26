package com.example.eightyage.domain.coupon.entity;

import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coupon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    private CouponState state;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;
}
