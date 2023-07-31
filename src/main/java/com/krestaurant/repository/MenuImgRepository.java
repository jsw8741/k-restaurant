package com.krestaurant.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krestaurant.entity.MenuImg;

public interface MenuImgRepository extends JpaRepository<MenuImg, Long>{
	MenuImg findByMenuIdOrderByIdAsc(Long menuId);
	
}
