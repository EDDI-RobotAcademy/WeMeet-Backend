package com.example.demo.moim.controller.form;

import com.example.demo.moim.controller.form.moimReqForm.*;
import lombok.Getter;

import java.util.List;

@Getter
public class MoimReqForm {
    private BasicInfo basicInfo;
    private DestinationInfo destinationInfo;
    private ParticipantsInfo participantsInfo;
    private List<OptionInfo> optionsInfo;
    private stateInfo stateInfo;
    private PaymentInfo paymentInfo;
}
