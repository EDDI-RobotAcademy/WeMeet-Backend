package com.example.demo.user.service;

import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.security.service.RedisService;
import com.example.demo.security.utils.JwtUtil;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserRole;
import com.example.demo.user.form.UserResForm;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final private BCryptPasswordEncoder passwordEncoder;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private UserRoleRepository userRoleRepository;
    final private RedisService redisService;
    final private JwtUtil jwtUtil;
    @Override
    public boolean signUp(UserSignUpForm userSignUpForm) {
        final Optional<User> maybeUser = userRepository.findByEmail(userSignUpForm.getEmail());
        if (maybeUser.isPresent()) {
            log.debug("이미 등록된 회원이라 가입할 수 없습니다.");
            return false;
        }
        final User user = userSignUpForm.toUser(passwordEncoder.encode(userSignUpForm.getPassword()));
        userRepository.save(user);
        final Role role = roleRepository.findByRoleType(userSignUpForm.getRoleType()).get();
        final UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
        return true;
    }

    @Override
    public boolean signOut(HttpHeaders headers, String refreshToken) {
        String accessToken = Objects.requireNonNull(headers.get("Authorization")).toString().substring(7);

        redisService.deleteByKey(accessToken);
        redisService.deleteByKey(refreshToken);

        return true;
    }

    @Override
    public ResponseEntity getUserInfo() {
        String email = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findUser(email).get();

        UserResForm userResForm = UserResForm.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .roleType(user.getRole())
                .build();
        return ResponseEntity.ok()
                .body(userResForm);
    }

    @Override
    public Boolean checkNickname(String nickname) {
        final Optional<User> presentNickname = userRepository.findByNickname(nickname);
        if (presentNickname.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkEmail(String email) {
        final Optional<User> presentEmail = userRepository.findByEmail(email);
        if (presentEmail.isPresent()) {
            return true;
        }
        return false;
    }
}
