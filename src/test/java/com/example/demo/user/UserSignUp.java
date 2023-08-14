package com.example.demo.user;

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

import static com.example.demo.user.entity.RoleType.NORMAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserSignUp {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private UserRoleRepository mockUserRoleRepository;
    @Mock
    private BCryptPasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private UserServiceImpl mockUserService;

    @BeforeEach
    public void setup () throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Mocking: 일반회원 회원가입")
    public void 일반회원이_회원가입을_합니다() {
        UserSignUpForm signUpForm = new UserSignUpForm("test@test.com", "test1", NORMAL);
        final User user = signUpForm.toUser("test1");

        final RoleType roleType = signUpForm.getRoleType();

        final Role role = new Role(roleType);
        mockRoleRepository.save(role);

        final String encodedPassword = "encodedPassword";
        final User expectedUser = new User("test@test.com", encodedPassword);
        final UserRole expectedUserRole = new UserRole(expectedUser, role);

        // Mock 객체의 반환값을 설정합니다.
        when(mockUserRepository.save(user))
                .thenReturn(expectedUser);
        when(mockUserRoleRepository.save(new UserRole(user, role)))
                .thenReturn(expectedUserRole);

        when(mockUserService.signUp(signUpForm)).thenReturn(true);

        // 대상 객체를 생성합니다.
        final UserServiceImpl sut = new UserServiceImpl(mockPasswordEncoder, mockUserRepository, mockRoleRepository, mockUserRoleRepository);

        // 실제 구동
        final boolean actual = sut.signUp(signUpForm);

        // 예측 결과와 실제 데이터 비교
        assertTrue(actual);

        //이메일 중복 테스트 따로만들어서 확인하기~~~~~이야~~~~이건 save만할꺼얌>.<
    }
}
