package com.fundingflex.pay.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fundingflex.common.enums.Enums.EasyPayType;
import com.fundingflex.common.enums.Enums.PayType;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.pay.domain.dto.KakaoPayReadyRequestDTO;
import com.fundingflex.pay.domain.dto.KakaoPayReadyResponseDTO;
import com.fundingflex.pay.domain.dto.PayCancelDTO;
import com.fundingflex.pay.domain.dto.PaymentCompletionRequest;
import com.fundingflex.pay.domain.dto.PaymentRequest;
import com.fundingflex.pay.domain.dto.PaymentVerificationResponse;
import com.fundingflex.pay.sender.KakaoPayRequestSender;
import com.fundingflex.pay.service.KakaoPayService;
import com.fundingflex.pay.service.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

//@RestController - 카카오 페이 때 사용
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@Controller //- 포트원 토스페이트먼트때 사용
public class PayController {

	private final PaymentService paymentService;

	//새 창에서 열기로 해당 페이지를 열어주어야 함
	@GetMapping("/ready/{joinid}")
	public String selectPayType(@PathVariable("joinid") Long joinId, Model model) {
		model.addAttribute("joinId",joinId);
		return "pay/select-type";
	}

	@GetMapping("/CARD/{joinid}")
	public String payCard(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, PayType.CARD);
	}

	@GetMapping("/BANK/{joinid}")
	public String payBank(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, PayType.BANK);
	}
	
	@GetMapping("/VBANK/{joinid}")
	public String payVbank(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, PayType.VBANK);
	}
	
	@GetMapping("/PHONE/{joinid}")
	public String payPhone(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, PayType.PHONE);
	}
	
	@GetMapping("/KAKAOPAY/{joinid}")
	public String payKakaoPay(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, EasyPayType.KAKAOPAY);
	}
	
	@GetMapping("/NAVERPAY/{joinid}")
	public String payNaverPay(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, EasyPayType.NAVERPAY);
	}
	
	@GetMapping("/TOSSPAY/{joinid}")
	public String payTossPay(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, EasyPayType.TOSSPAY);
	}
	
	@GetMapping("/PAYCO/{joinid}")
	public String payPayco(@PathVariable("joinid") Long joinId, Model model) {
		return settingPayModel(joinId, model, EasyPayType.PAYCO);
	}

	private String settingPayModel(Long joinId, Model model, PayType payType) {
		model.addAttribute("payMethod",payType.name());
		model.addAttribute("easyPayProvider","null");
		return inputPayModelBaseAttribute(joinId, model);
	}
	
	private String settingPayModel(Long joinId, Model model, EasyPayType easyPayType) {
		model.addAttribute("payMethod","EASY_PAY");
		model.addAttribute("easyPayProvider",easyPayType.name());
		return inputPayModelBaseAttribute(joinId, model);
	}
	
	private String inputPayModelBaseAttribute(Long joinId, Model model) {
		model.addAttribute("joinId", joinId);
		model.addAttribute("orderName", paymentService.getFundingsDtoByJoinId(joinId));
		model.addAttribute("totalAmount", paymentService.getFundingJoinsFundingAmountByJoinId(joinId));
		model.addAttribute("storeId", paymentService.getApiKey());
		model.addAttribute("channelKey", paymentService.getApiSecret());
		return "pay/pay";
	}
	
//    @PostMapping("/prepare")
//    public String preparePayment(@RequestBody PaymentRequest paymentRequest) {
//        try {
//            return paymentService.processPayment(paymentRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }
    
    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<String> completePayment (@RequestBody Map<String, Object> paymentData, Model model) {
    	//String fundingName = (String) paymentData.get("orderName");
    	System.out.println("강훈 :" + paymentData);
    	Long amount = Long.parseLong( (String) paymentData.get("totalAmount"));
    	Long joinId = Long.parseLong( (String) paymentData.get("joinId"));
    	String payUuid = (String) paymentData.get("paymentId");
    	boolean isPaymentSuccessful = paymentService.verifyPayment(joinId, amount);

        if (isPaymentSuccessful) {
            
            paymentService.save(joinId,payUuid,amount);
            
            return ResponseEntity.ok("pay/result");
        } 

        return ResponseEntity.ok("pay/error");
    } 
    
    
    @GetMapping("/result")
	public String selectPayType() {
		return "pay/result";
	}
    
    @GetMapping("/cancel/{joinid}")
    public String cancelPay(@PathVariable("joinid") Long joinId) {
		paymentService.cancelPay(paymentService.createPayCancelDTO(joinId));
		return "pay/cancel";
    }

//    @GetMapping("/edit/{joinid}")
//    public String editPay(@PathVariable("joinid") Long joinId) {
//    	paymentService.cancelPay(paymentService.createPayCancelDTO(joinId));
//    	
//    	return "pay/ready/" + joinId;
//    }
    
    
//카카오 페이 결제    
    
	private final KakaoPayService kakaoPayService;
    
    @PostMapping("/kakao/ready")
    public RedirectView kakaoPayReady(@RequestBody KakaoPayReadyRequestDTO request) {
        KakaoPayReadyResponseDTO response = kakaoPayService.kakaoPayReady(request);

        if (response == null || response.getNext_redirect_pc_url() == null) {
            // 에러 페이지로 리다이렉트
            return new RedirectView("/error");
        }

        return new RedirectView(response.getNext_redirect_pc_url());
    }

    @RequestMapping("/kakao")
    public void kakaoPay(HttpServletResponse response) throws Exception {
        // 결제 준비 요청 및 응답 파싱 과정 생략

    	KakaoPayRequestSender kakaoPayRequestSender = new KakaoPayRequestSender();
    	
        String pcUrl = kakaoPayRequestSender.testSend();// 응답에서 받은 PC URL

        response.sendRedirect(pcUrl); // 클라이언트에게 리다이렉션 요청

        // 혹은 return "redirect:" + pcUrl; 을 사용하지 않고 response.sendRedirect()를 사용하여 클라이언트에게 리다이렉션 요청
    }
    
    @GetMapping("/kakao/success")
    public String success() {
        return "결제가 성공적으로 완료되었습니다.";
    }

    @GetMapping("/kakao/cancel")
    public String cancel() {
        return "결제가 취소되었습니다.";
    }

    @GetMapping("/kakao/fail")
    public String fail() {
        return "결제가 실패하였습니다.";
    }
    
    
}
