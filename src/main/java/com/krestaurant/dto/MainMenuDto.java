package com.krestaurant.dto;

import com.krestaurant.entity.Category;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainMenuDto {
	private Long id;
	
	private String menuNm;
	
	private String menuDetail;
	
	private String imgUrl;
	
	private Integer price;
	
	private Category category;
	
	@QueryProjection
	public MainMenuDto(Long id, String menuNm, String menuDetail, String imgUrl, Integer price, Category category) {
		
		this.id = id;
		this.menuNm = menuNm;
		this.menuDetail = menuDetail;
		this.imgUrl = imgUrl;
		this.price = price;
		this.category = category;
		
	}
}
