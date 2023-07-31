package com.krestaurant.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.krestaurant.dto.MemberFormDto;
import com.krestaurant.dto.MemberModifyDto;
import com.krestaurant.dto.MenuFormDto;
import com.krestaurant.dto.ReviewFormDto;
import com.krestaurant.entity.Member;
import com.krestaurant.entity.Menu;
import com.krestaurant.entity.Review;
import com.krestaurant.service.MemberService;
import com.krestaurant.service.MenuService;
import com.krestaurant.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final MenuService menuService;
	private final ReviewService reviewService;
	
	// 로그인
	@GetMapping(value = "/members/login")
	public String login() {
		return "member/memberLoginForm";
	}
	
	// 회원가입 화면(고객)
	@GetMapping(value = "/members/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	// 회원가입(고객)
	@PostMapping(value = "/members/new")
	public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		// @Valid : 유효성을 검증하려는 객체 앞에 붙인다.
		// BindingResult : 유효성 검증 후의 결과가 들어있다.
		if(bindingResult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "member/memberForm";
		}
		
		try {
			// MemberFormDto -> Member Entity, 비밀번호 암호화
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
			
		
		return "redirect:/";
	}
	
	// 회원가입 화면(관리자)
	@GetMapping(value = "/admin/new")
	public String AdminForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/adminForm";
	}
	
	// 가입(관리자)
	@PostMapping(value = "/admin/create")
	public String AdminForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		// @Valid : 유효성을 검증하려는 객체 앞에 붙인다.
		// BindingResult : 유효성 검증 후의 결과가 들어있다.
		if(bindingResult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "member/adminForm";
		}
		
		try {
			// MemberFormDto -> Member Entity, 비밀번호 암호화
			Member member = Member.createAdmin(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/adminForm";
		}
		
		return "redirect:/";
	}
		
	// 로그인 실패했을 때
	@GetMapping(value = "/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "member/memberLoginForm";
	}
	
	// 마이페이지
	@GetMapping(value = "/members/myPage")
	public String memberMyPage(Model model, Principal principal) {
		
		MemberModifyDto member = memberService.getMyPage(principal.getName());
		
		model.addAttribute("member", member);
		
		return "member/memberMypage";
	}
	
	// 마이페이지 수정 화면
	@GetMapping(value = "/members/myPage/modify")
	public String memberMyPageModify(Model model, Principal principal) {
		
		try {
			MemberModifyDto member = memberService.getMyPage(principal.getName());
			model.addAttribute("member", member);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage",  "마이페이지 수정 화면 불러오는 중 에러가 발생했습니다.");
			model.addAttribute("member", new MemberFormDto());
			return "member/memberMypageModify";
		}
		
		
		return "member/memberMypageModify";
	}
	
	// 내 정보 수정
	@PostMapping(value = "/members/myPage/update")
	public String updateMember(@Valid MemberModifyDto memberModifyDto, Model model, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "menu/menuModifyForm";
		}
		
		try {
			memberService.updateMember(memberModifyDto, principal.getName());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "정보 수정 중 에러가 발생했습니다.");
			return "redirect:/menu/menuModifyForm";
		}
		
		MemberModifyDto member = memberService.getMyPage(principal.getName());
		
		model.addAttribute("member", member);
		
		return "member/memberMypage";
	}
	
	// 탈퇴
	@DeleteMapping(value = "/members/{memberId}/delete")
	public @ResponseBody ResponseEntity memberDelete(@PathVariable("memberId") Long menuId,
			Principal principal) {
		
		memberService.deleteMember(menuId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// 비밀번호 수정 화면
	@GetMapping(value = "/members/machingPw")
	public String chaingePw() {
		return "member/memberMachingPw";
	}
	
	// 비밀번호 수정
	@PostMapping(value = "/members/myPage/updatePw")
	public String updatePassword(Model model, Principal principal, @RequestParam("password") String password) {
		
		Member member = memberService.findMember(principal.getName());
		
		member.setPassword(passwordEncoder.encode(password));
		member = memberService.updatePassword(member);
		
		model.addAttribute("member", member);
		
		return "member/memberMypage";
	}
	
	// 리뷰 작성 화면
	@GetMapping(value = "/members/review")
	public String reviewForm(Model model) {
		
		List<Menu> menus = menuService.getMenuList();
		
		model.addAttribute("menus", menus);
		model.addAttribute("reviewFormDto", new ReviewFormDto());
		
		return "member/reviewForm";
	}
	
	// 리뷰 등록
	@PostMapping(value = "/members/review/create")
	public String reviewCreate(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult, Model model,
							   Principal principal) {
		
		// @Valid : 유효성을 검증하려는 객체 앞에 붙인다.
		// BindingResult : 유효성 검증 후의 결과가 들어있다.
		if(bindingResult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "member/memberForm";
		}
		
		try {
			Member member = memberService.findMember(principal.getName());
			MenuFormDto menuFormDto = menuService.getMenuDtl(reviewFormDto.getMenuId());
			Menu menu = menuService.getMenuReview(reviewFormDto.getMenuId());
			
			Review review = Review.createReview(reviewFormDto, member, menu, menuFormDto.getMenuImgDto().getImgUrl());
		
			reviewService.saveReview(review);
			
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		return "redirect:/";
	}
	
	
}
