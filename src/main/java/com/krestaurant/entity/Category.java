package com.krestaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 엔티티 클래스로 정의
@Table(name="category") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Category {
	
	@Id
	@Column(name="category_id")
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String category;
	
	public static Category createCategory(Long id, String categorySt) {
		Category category = new Category();
		
		category.id = id;
		category.category = categorySt;
		
		return category;
	}
}
