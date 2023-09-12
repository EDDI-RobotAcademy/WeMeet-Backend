package com.example.demo.payment.service;

import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.MoimPaymentInfo;
import com.example.demo.moim.entity.Participant;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.moim.repository.ParticipantRepository;
import com.example.demo.payment.controller.dto.PaymentReqForm;
import com.example.demo.payment.controller.dto.WebHookToken;
import com.example.demo.payment.entity.Installment;
import com.example.demo.payment.entity.Payment;
import com.example.demo.payment.repository.InstallmentRepository;
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
    final InstallmentRepository installmentRepository;

    @Value("${imp_key}")
    private String impKey;
    @Value("${imp_secret}")
    private String impSecret;

    @Override
    public ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId) {
        Optional<Moim> moim = moimRepository.findByIdJoinFetchPaymentInfo(moimId);
        if (moim.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        MoimPaymentInfo paymentInfo = moim.get().getMoimPaymentInfo();
        Long totalPrice = paymentInfo.getTotalPrice();
        Long AmountInstallment = paymentInfo.getAmountInstallment();
        Integer numInstallments = paymentInfo.getNumInstallments();

        Map<String, Object> responseMap = Map.of(
                "totalPrice", totalPrice,
                "amountInstallment", AmountInstallment,
                "numInstallments", numInstallments

        );
        return ResponseEntity.ok(responseMap);
    }


    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> firstPay(Long moimId, PaymentReqForm reqForm) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        log.info(String.valueOf(reqForm));
        Participant participant = participantRepository.findByUserAndMoimId(user, moimId);
        Moim moim = moimRepository.findById(moimId).get();

        Installment installment = Installment.builder()
                .merchantUid(reqForm.getMerchantUid())
                .build();

        Payment payment = Payment.builder()
                .customerUid(reqForm.getCustomerUid())
                .payMethod(reqForm.getPayMethod())
                .pgProvider(reqForm.getPgProvider())
                .paymentInfo(moim.getMoimPaymentInfo())
                .numInstallments(moim.getMoimPaymentInfo().getNumInstallments())
                .installments(new ArrayList<>())
                .build();
        payment.getInstallments().add(installment);
        paymentRepository.save(payment);

        installment.setPayment(payment);
        installmentRepository.save(installment);

        moim.getMoimPaymentInfo().getPaymentList().add(payment);
        moim.getParticipantsInfo().getParticipants().stream().filter((p) -> p.getUser().getId().equals(user.getId())).findFirst().get().setPayment(payment);
        moimRepository.save(moim);

        Map<String, Object> responseMap = Map.of("success", true);
        return ResponseEntity.ok(responseMap);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> webHook(@RequestBody WebHookToken token) {
        if (token.getStatus().equals("paid")) {
            getPaymentInfo(token);

            Installment installment = installmentRepository.findByMerchantUid(token.getMerchant_uid());
            Payment payment = installment.getPayment();
            if (payment.getInstallments().size() < payment.getNumInstallments()) {
                reserveNextPay(token);
            }
        }
//        String
        return ResponseEntity.ok().build();
    }

    @Override
    public Boolean withdraw(Participant participant, Moim moim) {
        Payment payment = paymentRepository.findByParticipantAndMoim(participant, moim);
        List<Installment> installments = payment.getInstallments();
        installmentRepository.deleteAll(installments);
        //기능 아직 구현 못함
        // api 이슈 확인 WMB-22
//        cancelReservedPay(installments);
        cancelPaidInstallments(installments);
        paymentRepository.delete(payment);
        return true;
    }

    private void cancelReservedPay(List<Installment> installments) {
        Installment reservedPay = installments.stream().filter((i) -> i.getReceipt_url() == null).findFirst().orElse(null);
        if (reservedPay != null) {
            String url = "https://api/iamport.kr/subscribe/payments/unschedule";
            String accessToken = getAccessToken();
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);

            RestTemplate restTemplate = new RestTemplate();
            String merchantUid = reservedPay.getMerchantUid();
            String customerUid = reservedPay.getPayment().getCustomerUid();

            JsonArray merchantArray = new JsonArray();
            merchantArray.add(merchantUid);

            JsonObject requestJson = new JsonObject();
            requestJson.add("merchant_uid", merchantArray);
            requestJson.addProperty("customer_uid", customerUid);

            HttpEntity requestEntity = new HttpEntity<String>(requestJson.toString(), headers);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestEntity, Map.class);
        }
    }

    private void cancelPaidInstallments(List<Installment> installments) {
        installments.stream().filter((i) -> i.getReceipt_url() != null).forEach(this::cancelPaidInstallment);
    }

    private void cancelPaidInstallment(Installment installment) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/payments/cancel";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        String merchantUid = installment.getMerchantUid();
        String reason = "고객 환불";

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("merchant_uid", merchantUid);
        requestJson.addProperty("reason", reason);

        HttpEntity requestEntity = new HttpEntity<String>(requestJson.toString(), headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestEntity, Map.class);
    }

    private void reserveNextPay(WebHookToken token) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/subscribe/payments/schedule";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        Installment installment = installmentRepository.findByMerchantUid(token.getMerchant_uid());
        Payment payment = installment.getPayment();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("customer_uid", payment.getCustomerUid());

        JsonObject schedulesData = new JsonObject();
        String merchantUid = UUID.randomUUID().toString();
        schedulesData.addProperty("merchant_uid", merchantUid);
        // millisecond to second 하기 위해 1000 나눠
        schedulesData.addProperty("schedule_at", Timestamp.valueOf(LocalDateTime.now().plusMinutes(1)).getTime() / 1000);
        log.info(String.valueOf(Timestamp.valueOf(LocalDateTime.now().plusMinutes(2)).getTime() / 1000));
        schedulesData.addProperty("amount", payment.getPaymentInfo().getAmountInstallment());
        schedulesData.addProperty("name", "get-moim");

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(schedulesData);
        requestJson.add("schedules", jsonArray);
        System.out.println(requestJson);
        HttpEntity requestEntity = new HttpEntity<String>(requestJson.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestEntity, Map.class);

        Installment newInstallment = Installment.builder()
                .merchantUid(merchantUid)
                .payment(payment)
                .build();

        installmentRepository.save(newInstallment);
        payment.getInstallments().add(newInstallment);
        paymentRepository.save(payment);
    }

    private void getPaymentInfo(WebHookToken token) {
        log.info("getPaymentInfo");
        String accessToken = getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + token.getImp_uid();
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity requestEntity = new HttpEntity<String>(null, headers);

        ResponseEntity<Map> installmentResponseMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, Map.class);
        Map<String, Map<String, Object>> responseBody = installmentResponseMap.getBody();
        Map<String, Object> responseMap = responseBody.get("response");
        log.info(responseMap.toString());
        Long amount = ((Integer) responseMap.get("amount")).longValue();
        String receiptUrl = (String) responseMap.get("receipt_url");

        Installment installment = installmentRepository.findByMerchantUid(token.getMerchant_uid());
        installment.setAmount(amount);
        installment.setReceipt_url(receiptUrl);
        installmentRepository.save(installment);
        installment.setImpUid(token.getImp_uid());

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
