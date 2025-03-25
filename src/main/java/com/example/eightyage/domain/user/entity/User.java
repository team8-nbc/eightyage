package com.example.eightyage.domain.user.entity;

import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userRole = UserRole.ROLE_USER;
    }
}
