package com.krestaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krestaurant.dto.ReviewFormDto;
import com.krestaurant.entity.Menu;
import com.krestaurant.entity.Review;
import com.krestaurant.repository.MenuRepository;
import com.krestaurant.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
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
	
	// 해당 메뉴의 리뷰 가져오기
	public List<Review> getReviewDtlList(Long menuId){
		List<Review> reviewList = reviewRepository.getReviewDtlList(menuId);
		
		return reviewList;
	}
	
	// 내가 쓴 리뷰 리스트
	public List<Review> getMyReview(Long memberId){
		List<Review> reviewList = reviewRepository.getMyReviewList(memberId);
		
		return reviewList;
	}
	
	// 수정할 리뷰
	public ReviewFormDto geyModifyReview(Long reivewId) {
		Review review = reviewRepository.findById(reivewId).orElseThrow(EntityNotFoundException::new);
	
		ReviewFormDto reviewFormDto = ReviewFormDto.of(review);
		
		return reviewFormDto;
	}
	
	// 리뷰 수정
	public Long updateReview(ReviewFormDto reviewFormDto, Menu menu, String imgUrl) throws Exception{
		Review review = reviewRepository.findById(reviewFormDto.getId())
										.orElseThrow(EntityNotFoundException::new);
		
		review.updateReview(reviewFormDto, menu, imgUrl);
		
		return review.getId();
	}
	
	// 리뷰 삭제
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
										.orElseThrow(EntityNotFoundException::new);
		
		reviewRepository.delete(review);
	}
}
