package com.krestaurant.entity;

import com.krestaurant.constant.MenuSellStatus;
import com.krestaurant.dto.MenuFormDto;
import com.krestaurant.exception.OutOfStockException;

import jakarta.persistence.*;
import lombok.*;

@Entity // 엔티티 클래스로 정의
@Table(name="menu") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Menu extends BaseEntity{
	@Id
	@Column(name="menu_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String menuNm;
	
	@Column(nullable = false)
	private int price;
	
	@Column(nullable = false)
	private int stockNumber;
	
	@Lob
	@Column(nullable = false)
	private String menuDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Enumerated(EnumType.STRING)
	private MenuSellStatus menuSellStatus;
	
	public void updateMenu(MenuFormDto menuFormDto, Category category) {
		this.menuNm = menuFormDto.getMenuNm();
		this.category = category;
		this.price = menuFormDto.getPrice();
		this.stockNumber = menuFormDto.getStockNumber();
		this.menuDetail = menuFormDto.getMenuDetail();
		this.menuSellStatus = menuFormDto.getMenuSellStatus();
		
	}
	
	public static Menu createMenu(Category category) {
		Menu menu = new Menu();
		menu.setCategory(category);
		
	return menu;	
	}
	
	// 재고를 감소시키는 메소드
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber; // 남은 재고 수량
		
		if(restStock < 0) {
			throw new OutOfStockException("메뉴의 재고가 부족합니다. 현재 재고 수량 : " + this.stockNumber);
		}
		
		this.stockNumber = restStock;
		
		if(this.stockNumber == 0) {
			this.menuSellStatus = MenuSellStatus.SOLD_OUT;
		}
	}
	
	// 재고를 증가시키는 메소드
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
		
		if(this.stockNumber > 0) {
			this.menuSellStatus = MenuSellStatus.SELL;
		}
		
	}
}
