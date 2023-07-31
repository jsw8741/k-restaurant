package com.krestaurant.dto;

import java.util.ArrayList;
import java.util.List;

import com.krestaurant.constant.BookStatus;
import com.krestaurant.entity.Reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	public ReservationHistDto(Reservation reservation) {
		this.bookId = reservation.getId();
		this.bookDate = reservation.getBookDate();
		this.bookMemberNm = reservation.getBookNm();
		this.bookMemberPh = reservation.getMember().getPhone();
		this.book_request = reservation.getBook_request();
		this.bookStatus = reservation.getBookStatus();
	}
	
	private Long bookId;
	
	private String bookMemberNm;
	
	private String bookMemberPh;

	private String bookDate;

	private String book_request;
	
	private BookStatus bookStatus;
	
	private List<BookMenuDto> bookMenuDtoList = new ArrayList<>();
	
	public void addBookMenuDto(BookMenuDto bookMenuDto) {
		this.bookMenuDtoList.add(bookMenuDto);
	}
}
