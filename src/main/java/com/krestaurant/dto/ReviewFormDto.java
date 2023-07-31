package com.krestaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewFormDto {
	private Long id;
	
	@NotNull(message = "리뷰 메뉴는 필수 선택입니다.")
	private Long menuId;
	
	@NotBlank(message = "리뷰 내용은 필수 입력입니다.")
	private String menuReview;
	
	private String imgUrl;

	private String addDate;
	
	private String updateDate;
}
