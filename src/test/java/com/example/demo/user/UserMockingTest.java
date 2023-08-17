package com.example.demo.user;

import com.example.demo.security.service.RedisService;
import com.example.demo.security.utils.JwtUtil;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserRole;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserRoleRepository;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserMockingTest {
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private UserRoleRepository mockUserRoleRepository;
    @Mock
    private RedisService mockRedisService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl mockUserService;

    @BeforeEach
    public void setup () throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Mocking 테스트를 준비합니다.")
    void test () {
        System.out.println("준비 됐니?");
    }

    @Test
    @DisplayName("Mocking: 일반회원 회원가입 테스트")
    public void 일반회원이_회원가입을_진행합니다 () {
        // 회원 등록 폼
        final UserSignUpForm userSignUpForm = new UserSignUpForm(
                "test","oh","test@test.com", "1234", RoleType.NORMAL);

        // 이메일 중복 확인
        when(mockUserRepository.findByEmail(userSignUpForm.getEmail()))
                .thenReturn(Optional.empty());

        // 계정 생성
        // 패스워드는 일단 암호화하지 않을게요
        final User user = userSignUpForm.toUser(userSignUpForm.getPassword());
        when(mockUserRepository.save(user))
                .thenReturn(new User("test@test.com", "1234"));

        // 회원 타입 부여
        final Role role = new Role(RoleType.NORMAL);
        when(mockRoleRepository.findByRoleType(userSignUpForm.getRoleType()))
                .thenReturn(Optional.of(role));
        final UserRole userRole = new UserRole(user, role);
        when(mockUserRoleRepository.save(userRole))
                .thenReturn(new UserRole(user, role));

        // 실제 구동 테스트
        final UserServiceImpl sut = new UserServiceImpl(
                passwordEncoder, mockUserRepository, mockRoleRepository, mockUserRoleRepository, mockRedisService, jwtUtil);
        final Boolean actual = sut.signUp(userSignUpForm);

//        assertTrue(actual);
        System.out.println(actual);
    }
}
