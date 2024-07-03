package com.fundingflex.pay.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fundingflex.pay.domain.dto.KakaoPayReadyRequestDTO;
import com.fundingflex.pay.domain.dto.KakaoPayReadyResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    @Value("${kakao.pay.admin-key}")
    private String adminKey;

    @Value("${kakao.pay.approval-url}")
    private String approvalUrl;

    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.fail-url}")
    private String failUrl;

    private static final String HOST = "https://kapi.kakao.com";

    public KakaoPayReadyResponseDTO kakaoPayReady(KakaoPayReadyRequestDTO request) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", request.getCid());
        params.add("partner_order_id", request.getPartner_order_id());
        params.add("partner_user_id", request.getPartner_user_id());
        params.add("item_name", request.getItem_name());
        params.add("quantity", request.getQuantity().toString());
        params.add("total_amount", request.getTotal_amount().toString());
        params.add("tax_free_amount", request.getTax_free_amount().toString());

        if (request.getVat_amount() != null) {
            params.add("vat_amount", request.getVat_amount().toString());
        }
        if (request.getGreen_deposit() != null) {
            params.add("green_deposit", request.getGreen_deposit().toString());
        }
        params.add("approval_url", request.getApproval_url() != null ? request.getApproval_url() : approvalUrl);
        params.add("cancel_url", request.getCancel_url() != null ? request.getCancel_url() : cancelUrl);
        params.add("fail_url", request.getFail_url() != null ? request.getFail_url() : failUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + adminKey);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            KakaoPayReadyResponseDTO KakaoPayReadyResponseDTO = restTemplate.postForObject(
                new URI(HOST + "/v1/payment/ready"),
                body,
                KakaoPayReadyResponseDTO.class
            );

            if (KakaoPayReadyResponseDTO == null) {
                log.error("KakaoPayReadyResponseDTO is null");
                return null;
            }

            log.info("KakaoPayReadyResponseDTO: {}", KakaoPayReadyResponseDTO);
            return KakaoPayReadyResponseDTO;
        } catch (Exception e) {
            log.error("Exception occurred while requesting Kakao Pay: ", e);
            return null;
        }
    }
}
