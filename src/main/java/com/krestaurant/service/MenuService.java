package com.krestaurant.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.krestaurant.dto.MainMenuDto;
import com.krestaurant.dto.MenuFormDto;
import com.krestaurant.dto.MenuImgDto;
import com.krestaurant.dto.MenuSearchDto;
import com.krestaurant.entity.Category;
import com.krestaurant.entity.Menu;
import com.krestaurant.entity.MenuImg;
import com.krestaurant.repository.CategoryRepository;
import com.krestaurant.repository.MenuImgRepository;
import com.krestaurant.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuImgRepository menuImgRepository;
	private final MenuImgService menuImgService;
	private final CategoryRepository categoryRepository;
	
	// menu 등록
	public Long saveMenu(MenuFormDto menuFormDto, List<MultipartFile> menuImgFileList) throws Exception{
			
			// 1. 메뉴등록
			Category category = categoryRepository.findById(menuFormDto.getCategoryId())
		   										   .orElseThrow(EntityNotFoundException::new);
			Menu menu = menuFormDto.createMenu(category); // dto -> entity
			menuRepository.save(menu); // insert(저장)
			// 2. 이미지등록
			for(int i =0; i<menuImgFileList.size(); i++) {
				// fk키를 사용시 부모테이블에 해당하는 entity를 먼저 넣어줘야한다.
				MenuImg menuImg = new MenuImg();
				menuImg.setMenu(menu);
			
				menuImgService.saveMenuImg(menuImg, menuImgFileList.get(i));
			}
			
			return menu.getId(); // 등록한 메뉴 id를 리턴
		}
	
	// menu 관리
	public Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto, Pageable pageable){
		Page<Menu> menuPage = menuRepository.getAdminMenuPage(menuSearchDto, pageable);
		
		return menuPage;
	}
	
	// menu 리스트 가져오기
	public List<Menu> getMenuList(){
		List<Menu> menuList = menuRepository.findAll();
		
		return menuList;
	}
	
	// 메뉴 상세 정보 가져오기
	@Transactional(readOnly = true)
	public MenuFormDto getMenuDtl(Long menuId) {
		MenuImg menuImg = menuImgRepository.findByMenuIdOrderByIdAsc(menuId);
		
		MenuImgDto menuImgDto = MenuImgDto.of(menuImg);
		
		Menu menu = menuRepository.findById(menuId)
								  .orElseThrow(EntityNotFoundException::new);
		
		MenuFormDto menuFormDto = MenuFormDto.of(menu);
		
		menuFormDto.setMenuImgDto(menuImgDto);
		
		return menuFormDto;
	}
	
	// 메뉴 수정
	public Long updateMenu(MenuFormDto menuFormDto, List<MultipartFile> menuImgFileList) throws Exception{
		Category category = categoryRepository.findById(menuFormDto.getCategoryId())
				   .orElseThrow(EntityNotFoundException::new);
		
		Menu menu = menuRepository.findById(menuFormDto.getId())
								  .orElseThrow(EntityNotFoundException::new);

		menu.updateMenu(menuFormDto, category);
		
		List<Long> menuImgIds = menuFormDto.getMenuImgIds();
		
		for(int i=0; i<menuImgFileList.size(); i++) {
			menuImgService.updateMenuImg(menuImgIds.get(i), menuImgFileList.get(i));
		}
		
		return menu.getId();
	}
	
	// 메뉴 삭제
	public void adminDeletMenu(Long menuId) {
		Menu menu = menuRepository.findById(menuId)
								  .orElseThrow(EntityNotFoundException::new);
	
		menuRepository.delete(menu);
	}
	
	// main 메뉴 리스트 가져오기
	public List<MainMenuDto> mainMenuList(){
		List<MainMenuDto> mainMenuDto = menuRepository.getMainMenuList();
		
		return mainMenuDto;
	}
	
	// 카테고리 리스트
	public List<Category> getCategoryList(){
		List<Category> categoryList = categoryRepository.findAll();
		
		return categoryList;
	}
	
	// 서버 최초 실행 시 카테고리 등록
	public Category saveCategory(Category category) {
		Category saveCategory = categoryRepository.save(category);
		
		return saveCategory;
	}

	// 리뷰 메뉴 가져오기
	@Transactional(readOnly = true)
	public Menu getMenuReview(Long menuId) {
		Menu menu = menuRepository.findById(menuId)
								  .orElseThrow(EntityNotFoundException::new);
		return menu;
	}
}
