package com.krestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krestaurant.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom{
	
	
}
