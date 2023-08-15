package com.example.demo.user;

import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.User;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiMockingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    @DisplayName("회원가입 api 테스트")
    public void 회원가입_api_테스트() throws Exception {
        final UserSignUpForm userSignUpForm = new UserSignUpForm("test@test.com", "1234", RoleType.NORMAL);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userSignUpForm);

        when(mockUserService.signUp(any(UserSignUpForm.class))).thenReturn(true);

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("true"));


        verify(mockUserService, times(1)).signUp(any(UserSignUpForm.class));
    }
}
