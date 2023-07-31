package com.krestaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation_menu")
@Getter
@Setter
@ToString
public class BookMenu extends BaseEntity{

	@Id
	@Column(name = "reservation_menu_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Reservation reservation;
	
	private int count;
	
	public static BookMenu createBookMenu(Menu menu, int count) {
		BookMenu bookMenu = new BookMenu();
		bookMenu.setMenu(menu);
		bookMenu.setCount(count);
		
		menu.removeStock(count);
		
		return bookMenu;
	}
	
	public void cancel() {
		this.getMenu().addStock(count);
	}
}
