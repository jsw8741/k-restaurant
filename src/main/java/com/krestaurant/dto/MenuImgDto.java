package com.krestaurant.dto;

import org.modelmapper.ModelMapper;

import com.krestaurant.entity.MenuImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuImgDto {
private Long id;
	
	private String imgName;
	
	private String oriImgName; //원본 이미지 파일명
	
	private String imgUrl; //이미지 조회 경로
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static MenuImgDto of(MenuImg menuImg) {
		return modelMapper.map(menuImg, MenuImgDto.class);
	}
}
