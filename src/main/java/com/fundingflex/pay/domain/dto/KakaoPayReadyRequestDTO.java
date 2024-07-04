package com.fundingflex.pay.domain.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class KakaoPayReadyRequestDTO {
	//타입, 이름 , 				// 필수 여부, 		설명
	String cid;					// O			가맹점 코드, 10자
	String cid_secret; 			// X			가맹점 코드 인증키, 24자, 숫자와 영문 소문자 조합
	String partner_order_id; 	// O			가맹점 주문번호, 최대 100자
	String partner_user_id;		// O			가맹점 회원 id, 최대 100자
	String item_name;			// O			상품명, 최대 100자
	String item_code;			// X			상품코드, 최대 100자
	Integer quantity;			// O			상품 수량
	Integer total_amount;		// O			상품 총액
	Integer tax_free_amount;	// O			상품 비과세 금액
	Integer vat_amount;			// X			상품 부가세 금액
	/*값을 보내지 않을 경우 다음과 같이 VAT 자동 계산
	(상품총액 - 상품 비과세 금액)/11 : 소수점 이하 반올림*/
	Integer green_deposit;		// X			컵 보증금
	String approval_url;		// O			결제 성공 시 redirect url, 최대 255자
	String cancel_url;			// O			결제 취소 시 redirect url, 최대 255자
	String fail_url;			// O			결제 실패 시 redirect url, 최대 255자
	List<String> available_cards;// X			결제 수단으로써 사용 허가할 카드사를 지정해야 하는 경우 사용
	/*카카오페이와 사전 협의 필요
	사용 허가할 카드사 코드*의 배열
	ex) ["HANA", "BC"]
	(기본값: 모든 카드사 허용)*/
	String payment_method_type;	// X			사용 허가할 결제 수단, 지정하지 않으면 모든 결제 수단 허용
	/*CARD 또는 MONEY 중 하나*/
	Integer install_month;		// X			카드 할부개월, 0~12
	String use_share_installment;// X			분담무이자 설정 (Y/N), 사용 시 사전 협의 필요
	Map<String,String> custom_json;		// X
	/*사전에 정의된 기능
	1. 결제 화면에 보여줄 사용자 정의 문구, 카카오페이와 사전 협의 필요
	2. iOS에서 사용자 인증 완료 후 가맹점 앱으로 자동 전환 기능(iOS만 처리가능, 안드로이드 동작불가)
	ex) return_custom_url과 함께 key 정보에 앱스킴을 넣어서 전송
	"return_custom_url":"kakaotalk://"*/
}
