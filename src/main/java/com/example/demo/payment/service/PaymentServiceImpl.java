package com.example.demo.payment.service;

import com.example.demo.moim.controller.form.MoimResForm;
import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.Participant;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.moim.repository.ParticipantRepository;
import com.example.demo.payment.controller.dto.PaymentReqForm;
import com.example.demo.payment.entity.Installment;
import com.example.demo.payment.entity.Payment;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.entity.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:portOne.properties")
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    final PaymentRepository paymentRepository;
    final ParticipantRepository participantRepository;
    final MoimRepository moimRepository;

    @Value("${imp_key}")
    private String impKey;
    @Value("${imp_secret}")
    private String impSecret;

    @Override
    public ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId) {
        Moim moim = moimRepository.findById(moimId).get();

        Map<String, Object> responseMap = Map.of("totalPrice", moim.getTotalPrice());
        return ResponseEntity.ok(responseMap);
    }

    @Override
    public void secondaryPay() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.iamport.kr/subscribe/payments/again";
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("customer_uid", "65e77852-1396-40c7-83c0-ad639d019ec1");
        requestJson.addProperty("pg", "danal_tpay");
        requestJson.addProperty("pay_method", "card");
        requestJson.addProperty("merchant_uid", String.valueOf(UUID.randomUUID()));
        requestJson.addProperty("name", "GET-MOIM");
        requestJson.addProperty("amount", 1000);
        requestJson.addProperty("buyer_name", "user.name");

        HttpEntity entity = new HttpEntity<String>(requestJson.toString(), headers);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, Map.class);
        Map responseMap = responseEntity.getBody();
        System.out.println(responseMap);
    }

    @Override
    public ResponseEntity<Map<String, Object>> firstPay(Long moimId, PaymentReqForm reqForm) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Participant participant = participantRepository.findByUserAndMoimId(user, moimId);
        Moim moim = moimRepository.findById(moimId).get();
        Payment payment = Payment.builder()
                .participant(participant)
                .customerUid(reqForm.getCustomerUid())
                .pgProvider(reqForm.getPgProvider())
                .payMethod(reqForm.getPayMethod())
                .installment(new ArrayList<>())
                .totalPrice(reqForm.getTotalPrice())
                .numInstallments(moim.getState().getRunwayPeriod())
                .build();

        Installment firstInstallment = Installment.builder()
                .amount(reqForm.getPaidAmount())
                .merchantUid(reqForm.getMerchantUid())
                .payment(payment)
                .build();
        payment.getInstallment().add(firstInstallment);
        paymentRepository.save(payment);

//        reservePays(payment);

        Map<String, Object> responseMap = Map.of("success", true);
        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<Map<String, Object>> webHook(@RequestBody Map req) {
        log.info("web-hook()");
        log.info(req.toString());
        return ResponseEntity.ok().build();
    }

    private void reservePays(Payment payment) {
        for (int i = 1; i < payment.getNumInstallments(); i++) {
            reserveNextPay(payment, i);
        }
    }

    private void reserveNextPay(Payment payment, int i) {

        log.info("reserveNextPay()");
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/subscribe/payments/schedule";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("customer_uid", payment.getCustomerUid());

        JsonObject schedulesData = new JsonObject();
        schedulesData.addProperty("merchant_uid", UUID.randomUUID().toString());
        // millisecond to second 하기 위해 1000 나눠
        schedulesData.addProperty("schedule_at", Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)).getTime() / 1000);
        log.info(String.valueOf(Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)).getTime() / 1000));
        schedulesData.addProperty("amount", payment.getInstallment().get(0).getAmount());
        schedulesData.addProperty("name", "get-moim");

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(schedulesData);
        requestJson.add("schedules", jsonArray);
        System.out.println(requestJson);
        HttpEntity requestEntity = new HttpEntity<String>(requestJson.toString(), headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestEntity, Map.class);
    }

    private String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.iamport.kr/users/getToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("imp_key", impKey);
        requestJson.addProperty("imp_secret", impSecret);

        log.info(impKey);
        log.info(impSecret);

        HttpEntity entity = new HttpEntity<String>(requestJson.toString(), headers);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, Map.class);
        Map responseMap = responseEntity.getBody();
        Map responseData = (Map) responseMap.get("response");
        String accessToken = (String) responseData.get("access_token");
        return accessToken;
    }
}
