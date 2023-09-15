package com.krestaurant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.krestaurant.constant.Role;
import com.krestaurant.dto.BookMenuDto;
import com.krestaurant.dto.ReservationDto;
import com.krestaurant.dto.ReservationHistDto;
import com.krestaurant.entity.BookMenu;
import com.krestaurant.entity.Member;
import com.krestaurant.entity.Menu;
import com.krestaurant.entity.MenuImg;
import com.krestaurant.entity.Reservation;
import com.krestaurant.repository.BookRepository;
import com.krestaurant.repository.MemberRepository;
import com.krestaurant.repository.MenuImgRepository;
import com.krestaurant.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
	private final MenuRepository menuRepository;
	private final MenuImgRepository menuImgRepository;
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;
	
	// 예약 등록
	public Long createbook(ReservationDto reservationDto, String email) {
		// 1. 예약할 메뉴 조회
		Menu menu = menuRepository.findById(reservationDto.getMainMenuId())
								  .orElseThrow(EntityNotFoundException::new);
		
		// 2. 현재 로그인한 회원 이메일을 이용하여 정보 조회
		Member member = memberRepository.findByEmail(email);
		
		// 3. 예약할 메뉴 엔티티와 예약 수량을 이용하여 예약 메뉴 엔티티 생성
		List<BookMenu> bookMenuList = new ArrayList<>();
		
		BookMenu bookMenu = BookMenu.createBookMenu(menu, reservationDto.getCount());
		bookMenuList.add(bookMenu);
		
		// 4. 회원 정보와 예약할 메뉴 리스트 정보를 이용하여 엔티티를 생성
		reservationDto.createReservation();
		Reservation reservation = Reservation.createBook(member, bookMenuList, reservationDto);
		bookRepository.save(reservation);
		
		return reservation.getId();
	}
	
	// 관리자 예약 목록
	@Transactional(readOnly = true)
	public Page<ReservationHistDto> getBookList(Pageable pageable){
		List<Reservation> bookPage = bookRepository.findReservations(pageable);
		
		Long count = bookRepository.countReservation();
		
		List<ReservationHistDto> reservationHistDtoList = new ArrayList<>();
		for(Reservation reservation : bookPage) {
			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation);
			List<BookMenu> bookMenuList = reservation.getBookMenus();
			
			for(BookMenu bookMenu : bookMenuList) {
				MenuImg menuImg = menuImgRepository.findByMenuIdOrderByIdAsc(bookMenu.getMenu().getId());
				
				BookMenuDto bookMenuDto = new BookMenuDto(bookMenu, menuImg.getImgUrl());
				reservationHistDto.addBookMenuDto(bookMenuDto);				
			}
			
			reservationHistDtoList.add(reservationHistDto);
		}
		
		return new PageImpl<>(reservationHistDtoList, pageable, count);
	}

	// 관리자 확인
	public boolean validateAdmin(String email) {
		Member curMember = memberRepository.findByEmail(email);
		
		if(curMember.getRole() != Role.ADMIN) {
			return false;
		}
		return true;
	}
	
	// 예약 취소
	public void cancelBook(Long bookId) {
		Reservation reservation = bookRepository.findById(bookId)
												.orElseThrow(EntityNotFoundException::new);
	
		reservation.cancelReservation();
	}
	
	// 예약 삭제
	public void deleteBook(Long bookId) {
		Reservation reservation = bookRepository.findById(bookId)
				.orElseThrow(EntityNotFoundException::new);
	
		bookRepository.delete(reservation);
	}
	
	// 내 예약 목록
	@Transactional(readOnly = true)
	public List<ReservationHistDto> myBookList(String email){
		Member member = memberRepository.findByEmail(email);
		
		List<Reservation> memberBookList = bookRepository.getMemberBookList(member.getId());
		List<ReservationHistDto> reservationHistDtoList = new ArrayList<>();
		
		for(Reservation reservation : memberBookList) {
			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation);
			List<BookMenu> bookMenuList = reservation.getBookMenus();
			
			for(BookMenu bookMenu : bookMenuList) {
				MenuImg menuImg = menuImgRepository.findByMenuIdOrderByIdAsc(bookMenu.getMenu().getId());
				
				BookMenuDto bookMenuDto = new BookMenuDto(bookMenu, menuImg.getImgUrl());
				reservationHistDto.addBookMenuDto(bookMenuDto);				
			}
			
			reservationHistDtoList.add(reservationHistDto);
		}
		
		
		return reservationHistDtoList;
	}
	
	// 예약자 본인 확인
	public boolean validateUser(String email, Long bookId) {
		Member curMember = memberRepository.findByEmail(email);
		
		Reservation reservation = bookRepository.findById(bookId)
								  .orElseThrow(EntityNotFoundException::new);
		
		Member savedMember = reservation.getMember();
		
		// 로그인한 사용자와 예약 고객의 이메일 확인
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
	}
}
