package com.example.demo.moim.controller.form;

import com.example.demo.moim.controller.form.moimReqForm.BasicInfo;
import com.example.demo.moim.controller.form.moimReqForm.DestinationInfo;
import com.example.demo.moim.controller.form.moimReqForm.ParticipantsInfo;
import lombok.Getter;

@Getter
public class MoimReqForm {
    private BasicInfo basicInfo;
    private DestinationInfo destinationInfo;
    private ParticipantsInfo participantsInfo;
}
