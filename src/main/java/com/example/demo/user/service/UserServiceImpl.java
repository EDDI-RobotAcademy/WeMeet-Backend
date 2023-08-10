package com.example.demo.user.service;

import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserRole;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final private BCryptPasswordEncoder passwordEncoder;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private UserRoleRepository userRoleRepository;
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
}
