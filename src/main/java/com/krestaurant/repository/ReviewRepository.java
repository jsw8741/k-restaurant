package com.krestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krestaurant.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}
