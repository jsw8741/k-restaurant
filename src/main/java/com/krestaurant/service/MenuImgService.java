package com.krestaurant.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.krestaurant.entity.MenuImg;
import com.krestaurant.repository.MenuImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuImgService {
	private String menuImgLocation = "C:/k-restaurant/menu";
	private final MenuImgRepository menuImgRepository;
	private final FileService fileService;
	
	public void saveMenuImg(MenuImg menuImg, MultipartFile menuImgFile) throws Exception{
		String oriImgName = menuImgFile.getOriginalFilename(); // 파일이름 -> 이미지1.jpg
		String imgName = "";
		String imgUrl = "";
		
		// 1. 파일을 menuImg Location에 저장
		if(!StringUtils.isEmpty(oriImgName)) {
			// oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(menuImgLocation, oriImgName, menuImgFile.getBytes());
			imgUrl = "/images/menu/" + imgName;
		}
		
		// 2. menu_img 테이블에 저장 
		
		menuImg.updateMenuImg(oriImgName, imgName, imgUrl);
		menuImgRepository.save(menuImg); // db에 insert
	}
	
	
	// 메뉴 이미지 업데이트
	public void updateMenuImg(Long menuImgId, MultipartFile menuImgFile)throws Exception{
		if(!menuImgFile.isEmpty()) {
			MenuImg savedMenuImg = menuImgRepository.findById(menuImgId)
											    .orElseThrow(EntityNotFoundException::new);
		
		if(!StringUtils.isEmpty(savedMenuImg.getImgName())) {
			fileService.deleteFile(menuImgLocation + "/" + savedMenuImg.getImgName());
		}
		
		String oriImgName = menuImgFile.getOriginalFilename();
		String imgName = fileService.uploadFile(menuImgLocation, oriImgName, menuImgFile.getBytes());
		
		String imgUrl = "/images/menu/" + imgName;
		
		savedMenuImg.updateMenuImg(oriImgName, imgName, imgUrl);
		
		}
	}
}
