package com.krestaurant.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.krestaurant.dto.KakaoPayApproveDto;
import com.krestaurant.dto.KakaoPayReadyDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

	public KakaoPayReadyDto kakaoPay(Map<String, Object> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK a729a14eaa765b52faa416faff03f519");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<String, Object>();
		
		payParams.add("cid", "TC0ONETIME");
		payParams.add("partner_order_id", "partner_order_id");
		payParams.add("partner_user_id", "partner_user_id");
		if(params.get("itemCount").equals(0)) {
			payParams.add("item_name", "OSTB("+params.get("itemName")+")"); 
		}else {
			payParams.add("item_name", "OSTB("+params.get("itemName") +" 외" + params.get("itemCount") + ")");
		}
		payParams.add("quantity", "1");
		payParams.add("total_amount", params.get("totalPrice")); //가격
		payParams.add("tax_free_amount", "0");
		if(params.get("int") == "1") {
			payParams.add("approval_url", "http://ec2-13-124-243-199.ap-northeast-2.compute.amazonaws.com/bookpay/success");
		}else {
			payParams.add("approval_url", "http://ec2-13-124-243-199.ap-northeast-2.compute.amazonaws.com/pay/success");
		}
		payParams.add("cancel_url", "http://ec2-13-124-243-199.ap-northeast-2.compute.amazonaws.com/pay/cancel");
		payParams.add("fail_url", "http://ec2-13-124-243-199.ap-northeast-2.compute.amazonaws.com/pay/fail");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(payParams, headers);
		
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		KakaoPayReadyDto res = template.postForObject(url, request, KakaoPayReadyDto.class);
		return res;
	}
	
	
	public KakaoPayApproveDto kakaoPayApprove(String tid, String pgToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK a729a14eaa765b52faa416faff03f519");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<String, Object>();
		
		payParams.add("cid", "TC0ONETIME");
		payParams.add("tid", tid);
		payParams.add("partner_order_id", "partner_order_id");
		payParams.add("partner_user_id", "partner_user_id");
		payParams.add("pg_token", pgToken);
		
		HttpEntity<Map> request = new HttpEntity<Map>(payParams, headers);
		
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";
		
		KakaoPayApproveDto res = template.postForObject(url, request, KakaoPayApproveDto.class);
		return res;
	}
}
