package com.example.demo.moim.controller.form;

import com.example.demo.user.controller.form.UserInfoResForm;
import com.example.demo.user.controller.form.UserResForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MoimInfoResForm {
    private Long id;
    private String title;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    private List<UserResForm> participants;
}
