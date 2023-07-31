package com.krestaurant.entity;

import java.util.ArrayList;
import java.util.List;

import com.krestaurant.constant.BookStatus;
import com.krestaurant.dto.ReservationDto;

import jakarta.persistence.*;
import lombok.*;

@Entity // 엔티티 클래스로 정의
@Table(name="reservations") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Reservation {
	
	@Id
	@Column(name="book_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String bookNm;
	
	@Column(nullable = false)
	private String bookDate;

	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;
	
	@Column(nullable = false)
	private Integer bookPersonnel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 연관관계의 주인 설정(외래키 지정)
	private List<BookMenu> bookMenus = new ArrayList<>();
	
	@Lob
	@Column(columnDefinition = "longtext")
	private String book_request;
	
	public void addBookMenu(BookMenu bookMenu) {
		this.bookMenus.add(bookMenu);
		bookMenu.setReservation(this);
	}
	
	public static Reservation createBook(Member member, List<BookMenu> bookMenuList, ReservationDto reservationDto) {
		Reservation reservation = new Reservation();
		
		reservation.setBookNm(reservationDto.getBookNm());
		reservation.setBookDate(reservationDto.getBookDate());
		reservation.setBookPersonnel(reservationDto.getBookPersonnel());
		reservation.setBook_request(reservationDto.getBook_request());
		reservation.setBookStatus(BookStatus.BOOK);
		reservation.setMember(member);
		
		for(BookMenu bookMenu : bookMenuList) {
			reservation.addBookMenu(bookMenu);
		}
		
		
		return reservation;
	}
	
	// 예약 취소
	public void cancelReservation() {
		this.bookStatus = BookStatus.CANCEL;
		
		for(BookMenu bookMenu : bookMenus) {
			bookMenu.cancel();
		}
		
	}
	
}
