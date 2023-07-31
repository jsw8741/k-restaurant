package com.krestaurant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.krestaurant.dto.MainMenuDto;
import com.krestaurant.dto.MenuSearchDto;
import com.krestaurant.entity.Menu;

public interface MenuRepositoryCustom {
	Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto, Pageable pageable);

	List<MainMenuDto> getMainMenuList();
}
