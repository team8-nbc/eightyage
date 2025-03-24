package com.example.eightyage.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String RefreshToken;

    @Enumerated(EnumType.STRING)
    private TokenState tokenState;

    public RefreshToken(Long userId) {
        this.userId = userId;
        this.RefreshToken = UUID.randomUUID().toString();
        this.tokenState = TokenState.VALID;
    }

    public void updateTokenStatus(TokenState tokenStatus){
        this.tokenState = tokenStatus;
    }
}
