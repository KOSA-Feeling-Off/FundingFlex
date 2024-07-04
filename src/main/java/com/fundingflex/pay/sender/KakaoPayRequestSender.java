package com.fundingflex.pay.sender;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fundingflex.pay.domain.dto.KakaoPayReadyRequestDTO;
import com.fundingflex.pay.domain.dto.KakaoPayReadyResponseDTO;

@Component
public class KakaoPayRequestSender {

	private static final String API_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
	private static final String SECRET_KEY = "DEV7432E37CFA51BF0068C880B462D8EE440247C";

	public String testSend() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        KakaoPayReadyRequestDTO request = new KakaoPayReadyRequestDTO();
        request.setCid("TC0ONETIME");
        request.setPartner_order_id("partner_order_id");
        request.setPartner_user_id("partner_user_id");
        request.setItem_name("초코파이");
        request.setQuantity(1);
        request.setTotal_amount(2200);
        request.setVat_amount(200);
        request.setTax_free_amount(0);
        request.setApproval_url("http://localhost/pay/success");
        request.setCancel_url("http://localhost/pay/cancel");
        request.setFail_url("http://localhost/pay/fail");

        HttpEntity<KakaoPayReadyRequestDTO> entity = new HttpEntity<>(request, headers);
        String payUrl = "";
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            System.out.println(response.getBody());
            
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoPayReadyResponseDTO kakaoPayResponse = objectMapper.readValue(response.getBody(), KakaoPayReadyResponseDTO.class);
            payUrl = kakaoPayResponse.getNext_redirect_pc_url();
            
        } catch (HttpClientErrorException e) {
            System.err.println("HttpClientErrorException: " + e.getStatusCode());
            System.err.println("Response body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return payUrl;
    }
}