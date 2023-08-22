package com.example.demo.user;

import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.UserRole;
import com.example.demo.user.controller.form.UserSignUpForm;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiMockingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository mockUserRepository;
    @MockBean
    private RoleRepository mockRoleRepository;
    @MockBean
    private UserRoleRepository userRoleRepository;

    @Test
    @DisplayName("회원가입 api 테스트")
    public void 회원가입_api_테스트() throws Exception {
        final UserSignUpForm userSignUpForm = new UserSignUpForm("test","oh","test@test.com", "1234", RoleType.NORMAL);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userSignUpForm);

        when(mockUserRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());
        when(mockRoleRepository.findByRoleType(userSignUpForm.getRoleType())).thenReturn(Optional.of(new Role(RoleType.NORMAL)));
        when(userRoleRepository.save(any())).thenReturn(null);

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("true"));


        verify(mockUserRepository, times(1)).findByEmail(any(String.class));
        verify(mockRoleRepository, times(1)).findByRoleType(any(RoleType.class));
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }
}
