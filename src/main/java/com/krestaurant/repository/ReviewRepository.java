package com.krestaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krestaurant.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	@Query("select r from Review r where r.menu.id = :menu_id")
	List<Review> getReviewDtlList(@Param("menu_id") Long menuId);
	
	@Query("select r from Review r where r.member.id = :member_id")
	List<Review> getMyReviewList(@Param("member_id") Long memberId);
}
