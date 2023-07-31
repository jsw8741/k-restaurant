package com.krestaurant.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.krestaurant.dto.ReservationDto;
import com.krestaurant.dto.ReservationHistDto;
import com.krestaurant.entity.Member;
import com.krestaurant.entity.Menu;
import com.krestaurant.service.BookService;
import com.krestaurant.service.MemberService;
import com.krestaurant.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;
	private final MenuService menuService;
	private final MemberService memberService;
	
	@GetMapping(value = "/book/page")
	public String bookPage(Model model) {
		ReservationDto reservationDto = new ReservationDto();
		List<Menu> menus = menuService.getMenuList();
		
		model.addAttribute("menus", menus);
		model.addAttribute("reservationDto", reservationDto);		
		
		return "book/bookPage";
	}
	
	// 예약하기
	@PostMapping(value = "/book/new")
	public @ResponseBody ResponseEntity bookForm(Model model, @RequestBody @Valid ReservationDto reservationDto, BindingResult bindingResult,
						   Principal principal){
		
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage() + "\n"); // 에러 메세지를 합친다.
			}
			
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		
		String email = principal.getName(); // id에 해당하는 정보를 가지고 온다(email)
		Long bookId;
		
		try {
			bookId = bookService.createbook(reservationDto, email);
			
		} catch (Exception e) {
			e.printStackTrace();
			List<Menu> menus = menuService.getMenuList();
			
			model.addAttribute("menus", menus);
			model.addAttribute("errorMessage",  "예약 중 에러가 발생했습니다.");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(bookId, HttpStatus.OK);
	}
	
	// 예약 리스트
	@GetMapping(value = {"/admin/books", "/admin/books/{page}"})
	public String bookHist(@PathVariable("page") Optional<Integer> page,
			Principal principal, Model model) {
		// 1. 페이지당 3개의 데이터를 가져오도록 설정
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		
		Page<ReservationHistDto> books = bookService.getBookList(pageable);
		model.addAttribute("books", books);
		model.addAttribute("maxPage", 5);
		
		return "book/bookHist";
	}
	
	// 관리자가 예약 취소
	@PostMapping(value = "/admin/book/{bookId}/cancel")
	public @ResponseBody ResponseEntity cancelBook(@PathVariable("bookId") Long bookId,
			Principal principal) {

		// 관리자인지 확인
		if(!bookService.validateAdmin(principal.getName())) {
			return new ResponseEntity<String>("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		bookService.cancelBook(bookId);
		
		return new ResponseEntity<Long>(bookId, HttpStatus.OK);
	}
	
	// 관리자가 예약 삭제
	@DeleteMapping(value = "/admin/book/{bookId}/delete")
	public @ResponseBody ResponseEntity adminDeleteBook(@PathVariable("bookId") Long bookId,
			Principal principal) {
		
		// 관리자인지 확인
		if(!bookService.validateAdmin(principal.getName())) {
			return new ResponseEntity<String>("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		bookService.deleteBook(bookId);
		
		return new ResponseEntity<Long>(bookId, HttpStatus.OK);
	}
	
	// 고객 예약 정보 화면
	@GetMapping(value = "/members/myBook")
	public String myBook(Principal principal, Model model) {
		
		Member member = memberService.findMember(principal.getName());
		List<ReservationHistDto> memberBookList = bookService.myBookList(principal.getName());
		
		
		model.addAttribute("member", member);
		model.addAttribute("memberBookList", memberBookList);
		
		return "member/memberMyBook";
	}
	
	// 고객이 예약 취소
	@PostMapping(value = "/book/{bookId}/cancel")
	public @ResponseBody ResponseEntity cancelOrder(@PathVariable("bookId") Long bookId,
			Principal principal) {
		
		// 본인 확인
		if(!bookService.validateUser(principal.getName(), bookId)) {
			return new ResponseEntity<String>("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		bookService.cancelBook(bookId);
		
		return new ResponseEntity<Long>(bookId, HttpStatus.OK);
	}
	
	// 고객이 예약 삭제
	@DeleteMapping(value = "/book/{bookId}/delete")
	public @ResponseBody ResponseEntity userDeleteBook(@PathVariable("bookId") Long bookId,
			Principal principal) {
		
		// 본인 확인
		if(!bookService.validateUser(principal.getName(), bookId)) {
			return new ResponseEntity<String>("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		bookService.deleteBook(bookId);
		
		return new ResponseEntity<Long>(bookId, HttpStatus.OK);
	}
}
