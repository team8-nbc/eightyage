package com.example.eightyage.domain.user.service;

import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.repository.UserRepository;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(String email, String nickname, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, nickname, encodedPassword);

        return userRepository.save(user);
    }
}
