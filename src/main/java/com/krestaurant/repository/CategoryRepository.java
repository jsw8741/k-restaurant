package com.krestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krestaurant.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
