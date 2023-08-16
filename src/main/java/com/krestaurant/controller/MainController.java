package com.krestaurant.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.krestaurant.dto.MainMenuDto;
import com.krestaurant.entity.Category;
import com.krestaurant.entity.Member;
import com.krestaurant.entity.Review;
import com.krestaurant.service.MemberService;
import com.krestaurant.service.MenuService;
import com.krestaurant.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MenuService menuService;
	private final ReviewService reviewService;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/")
	public String index(Model model, Authentication authentication) {
		
		// 로그인되지 않은 상태
		if (authentication == null || !authentication.isAuthenticated()) {
			// 서버 최초 실행 시 관리자 없으면 등록
			if(memberService.getAdminList().isEmpty()) {
				Member member = Member.createMaster(passwordEncoder);
				memberService.saveMember(member);
			}
			
			// 서버 최초 실행 시 등록된 카테고리 없으면 등록		
			if(menuService.getCategoryList().isEmpty()) {
				Category category1 = Category.createCategory((long) 1, "탕");
				Category category2 = Category.createCategory((long) 2, "볶음");
				Category category3 = Category.createCategory((long) 3, "찜");
				Category category4 = Category.createCategory((long) 4, "정식");
				
				menuService.saveCategory(category1);
				menuService.saveCategory(category2);
				menuService.saveCategory(category3);
				menuService.saveCategory(category4);
			}

	        model.addAttribute("loginMessage", "로그인 후 이용해주세요.");
	        List<Review> reviews = reviewService.getReviewList();
			List<MainMenuDto> menus = menuService.mainMenuList();
			
			
			model.addAttribute("reviews", reviews);
			model.addAttribute("menus", menus);
			
			return "index";
	    }
		
		List<Review> reviews = reviewService.getReviewList();
		List<MainMenuDto> menus = menuService.mainMenuList();
		
		model.addAttribute("reviews", reviews);
		model.addAttribute("menus", menus);
		
		return "index";
	}
	
	@GetMapping(value = "/about")
	public String about() {
		return "about/about";
	}
	
}
