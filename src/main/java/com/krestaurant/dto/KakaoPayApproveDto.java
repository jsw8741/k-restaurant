package com.krestaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayApproveDto {
	private String aid;
	private String tid;
	private String partner_order_is;
	private String payment_method_type;

}