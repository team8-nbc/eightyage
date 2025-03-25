package com.example.eightyage.domain.user.service;

import com.example.eightyage.domain.user.dto.request.UserDeleteRequest;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import com.example.eightyage.domain.user.repository.UserRepository;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 훠원저장 */
    @Transactional
    public User saveUser(String email, String nickname, String password, String userRole) {

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, nickname, encodedPassword, UserRole.of(userRole));

        return userRepository.save(user);
    }

    /* 회원탈퇴 */
    @Transactional
    public void deleteUser(AuthUser authUser, UserDeleteRequest request) {
        User findUser = findUserByIdOrElseThrow(authUser.getUserId());

        if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        findUser.deleteUser();
    }

    public User findUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException("가입한 유저의 이메일이 아닙니다.")
        );
    }

    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("해당 유저의 Id를 찾을 수 없습니다.")
        );
    }
}
