package com.fundingflex.pay.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.common.enums.Enums.Delete;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;
import com.fundingflex.mybatis.mapper.pay.PayMapper;
import com.fundingflex.pay.domain.dto.PayCancelDTO;
import com.fundingflex.pay.domain.dto.PayDTO;
import com.fundingflex.pay.domain.dto.PaymentRequest;
import com.fundingflex.pay.domain.dto.PaymentVerificationResponse;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class PaymentService {

    @Value("${portone.api_key}")
    private String apiKey;

    @Value("${portone.api_secret}")
    private String apiSecret;

    @Value("${portone.api.url}")
    private String portoneApiUrl;
    
    //private final OkHttpClient httpClient = new OkHttpClient();
    
    @Autowired
    private PayMapper payMapper;
    
    @Autowired
    private FundingsMapper fundingsMapper;
    
    public String getApiKey() {
    	return apiKey;
    }
    
    public String getApiSecret() {
    	return apiSecret;
    }

//    public String getToken() throws Exception {
//        RequestBody body = new FormBody.Builder()
//                .add("imp_key", apiKey)
//                .add("imp_secret", apiSecret)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(portoneApiUrl + "/users/getToken")
//                .post(body)
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response + " with message: " + response.body().string());
//            }
//
//            return response.body().string();
//        }
//    }
//
//    public String processPayment(PaymentRequest paymentRequest) throws Exception {
//        String token = getToken();
//
//        RequestBody body = new FormBody.Builder()
//                .add("merchant_uid", paymentRequest.getMerchantUid())
//                .add("amount", String.valueOf(paymentRequest.getAmount()))
//                .add("buyer_name", paymentRequest.getBuyerName())
//                .build();
//
//        Request request = new Request.Builder()
//                .url(portoneApiUrl + "/payments/prepare")
//                .post(body)
//                .addHeader("Authorization", "Bearer " + token)
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response + " with message: " + response.body().string());
//            }
//
//            return response.body().string();
//        }
//    }
    
    public String getFundingsDtoByJoinId(Long fundingJoinId) {
    	return payMapper.getFundingsTitleByJoinId(fundingJoinId);
    }
    
    public Long getFundingJoinsFundingAmountByJoinId(Long fundingJoinId) {
    	return payMapper.getFundingJoinsFundingAmountByJoinId(fundingJoinId);
    }


	public boolean verifyPayment(Long joinId, Long amount) {
		return amount.equals(payMapper.getFundingJoinsFundingAmountByJoinId(joinId));
	}

	public void save(Long joinId, String payUuid, Long amount) {
		PayDTO payDTO = new PayDTO();
		payDTO.setFundingJoinId(joinId);
		payDTO.setPayAmount(amount);
		payDTO.setPayTime(new Date());
		payDTO.setPayUuid(payUuid);
		payDTO.setUserId(1L);
		payDTO.setIsDeleted(Delete.N.name());
		payMapper.save(payDTO);
	}

	public PayCancelDTO createPayCancelDTO(Long joinId) {
		PayCancelDTO payCancelDTO = new PayCancelDTO();
		payCancelDTO.setFundingJoinsId(joinId);
		
		return payCancelDTO;
	}

	public void cancelPay(PayCancelDTO payCancelDTO)  {
		fundingsMapper.deleteFundingJoin(payCancelDTO.getFundingJoinsId());
		payMapper.updatePaymentsIsDeleted(payCancelDTO.getFundingJoinsId());
	}
}
