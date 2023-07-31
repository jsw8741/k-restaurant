package com.krestaurant.dto;

import com.krestaurant.entity.BookMenu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMenuDto {

	public BookMenuDto(BookMenu bookMenu, String imgUrl) {
		this.menuNm = bookMenu.getMenu().getMenuNm();
		this.count = bookMenu.getCount();
		this.imgUrl = imgUrl;
	}
	
	private String menuNm;
	
	private int count;
	
	private String imgUrl;
}
