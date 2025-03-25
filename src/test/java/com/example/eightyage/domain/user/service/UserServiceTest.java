package com.example.eightyage.domain.user.service;

import com.example.eightyage.domain.auth.entity.RefreshToken;
import com.example.eightyage.domain.auth.service.TokenService;
import com.example.eightyage.domain.user.dto.request.UserDeleteRequest;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.entity.UserRole;
import com.example.eightyage.domain.user.repository.UserRepository;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    /* findUserByIdOrElseThrow */
    @Test
    void findById조회_userId가_없을_경우_실패() {
        // given
        Long userId = 1L;

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class,
                () -> userService.findUserByIdOrElseThrow(userId),
                "해당 유저의 Id를 찾을 수 없습니다.");
    }

    @Test
    void findById조회_성공() {
        // given
        Long userId = 1L;
        User user = new User(userId, "email@email.com", "nickname", UserRole.ROLE_USER);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        User resultUser = userService.findUserByIdOrElseThrow(userId);

        // then
        assertNotNull(resultUser);
        assertEquals(user.getId(), resultUser.getId());
        assertEquals(user.getEmail(), resultUser.getEmail());
        assertEquals(user.getNickname(), resultUser.getNickname());
        assertEquals(user.getUserRole(), resultUser.getUserRole());
    }

    /* findUserByEmailOrElseThrow */
    @Test
    void findByEmail조회_email이_없을_경우_실패() {
        // given
        String email = "email@email.com";

        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // when & then
        assertThrows(UnauthorizedException.class,
                () -> userService.findUserByEmailOrElseThrow(email),
                "가입한 유저의 이메일이 아닙니다.");
    }

    @Test
    void findByEmail조회_성공() {
        // given
        String email = "email@email.com";
        User user = new User(1L, email, "nickname", UserRole.ROLE_USER);

        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        // when
        User resultUser = userService.findUserByEmailOrElseThrow(email);

        // then
        assertNotNull(resultUser);
        assertEquals(user.getId(), resultUser.getId());
        assertEquals(user.getEmail(), resultUser.getEmail());
        assertEquals(user.getNickname(), resultUser.getNickname());
        assertEquals(user.getUserRole(), resultUser.getUserRole());
    }

    /* saveUser */
    @Test
    void 회원저장_중복된_이메일이_있을_경우_실패() {
        // given
        String email = "email@email.com";
        String nickname = "nickname";
        String password = "password1234";

        given(userRepository.existsByEmail(any(String.class))).willReturn(true);

        // when & then
        assertThrows(BadRequestException.class,
                () -> userService.saveUser(email, nickname, password),
                "등록된 이메일입니다.");
    }

    @Test
    void 회원저장_성공() {
        // given
        String email = "email@email.com";
        String nickname = "nickname";
        String password = "password1234";
        User user = new User(email, nickname, password);

        String encodedPassword = "encoded-password1234";

        given(userRepository.existsByEmail(any(String.class))).willReturn(false);
        given(passwordEncoder.encode(any(String.class))).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        User resultUser = userService.saveUser(email, nickname, password);

        // then
        assertNotNull(resultUser);
        assertEquals(email, resultUser.getEmail());
        assertEquals(nickname, resultUser.getNickname());
        assertEquals(password, resultUser.getPassword());
    }

    /* deleteUser */
    @Test
    void 회원탈퇴_회원이_존재하지_않으면_실패() {
        // given
        AuthUser authUser = new AuthUser(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        UserDeleteRequest successDeleteDto = new UserDeleteRequest("password1234!");

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class,
                () -> userService.deleteUser(authUser, successDeleteDto),
                "해당 유저의 Id를 찾을 수 없습니다.");
    }

    @Test
    void 회원탈퇴_비밀번호가_일치하지_않으면_실패() {
        // given
        AuthUser authUser = new AuthUser(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        UserDeleteRequest successDeleteDto = new UserDeleteRequest("password1234!");
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "password", "password1234");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(false);

        // when & then
        assertThrows(UnauthorizedException.class,
                () -> userService.deleteUser(authUser, successDeleteDto),
                "비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 회원탈퇴_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        UserDeleteRequest successDeleteDto = new UserDeleteRequest("password1234");
        User user = new User(1L, "email@email.com", "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "password", "password1234");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

        // when
        userService.deleteUser(authUser, successDeleteDto);

        // then
        assertNotNull(user.getDeletedAt());

    }
}
