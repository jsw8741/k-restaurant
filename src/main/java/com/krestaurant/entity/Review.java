package com.krestaurant.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.krestaurant.dto.ReviewFormDto;

import jakarta.persistence.*;
import lombok.*;

@Entity // 엔티티 클래스로 정의
@Table(name="review") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Review extends BaseEntity{
	
	@Id
	@Column(name="review_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Lob
	@Column(nullable = false)
	private String reviewText;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private Menu menu;
	
	private String imgUrl;
	
	public static Review createReview(ReviewFormDto reviewFormDto, Member member, Menu Menu, String imgUrl) {
		Review review = new Review();
		
		review.setReviewText(reviewFormDto.getMenuReview());
		review.setMember(member);
		review.setMenu(Menu);
		review.setImgUrl(imgUrl);
		
		return review;
	}
}
