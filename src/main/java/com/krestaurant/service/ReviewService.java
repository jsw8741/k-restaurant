package com.krestaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krestaurant.entity.Review;
import com.krestaurant.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	// 리뷰 등록
	public Review saveReview(Review review) {
		Review saveReview = reviewRepository.save(review);
		
		return saveReview;
	}
	
	// 리뷰 리스트 가져오기
	public List<Review> getReviewList(){
		List<Review> reviewList = reviewRepository.findAll();
		
		return reviewList;
	}
}
