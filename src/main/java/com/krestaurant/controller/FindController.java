package com.krestaurant.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.krestaurant.entity.Member;
import com.krestaurant.service.EmailService;
import com.krestaurant.service.EmailServiceImpl;
import com.krestaurant.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FindController {
	
	private final EmailService emailService;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/find/pw")
	public String findPw() {
		
		return "find/findPw";
	}
	
	@PostMapping(value = "/find/emailConfirm")
	public String emailConfirm(@RequestParam("email") String email, Model model) throws Exception {
		
		Member member = new Member();
		try {
			// 1. 입력한 이메일이 회원인지 확인
			member = memberService.findMember(email);
			
			// 2. 입력한 메일로 임시 비밀번호 전송
			String tempPassword = EmailServiceImpl.createTempPassword();
			emailService.sendSimpleMessage(email, tempPassword);
			
			// 3. 기존 비밀번호를 임시 비밀번호로 변경
			member.setPassword(passwordEncoder.encode(tempPassword));
			member = memberService.updatePassword(member);
			model.addAttribute("tempPasswordMessage", "임시 비밀번호로 로그인해주세요.");
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "가입된 이메일이 아닙니다!");
			return "find/findPw";
		}
		
		return "member/memberLoginForm";
	}
	
	// 현재 비밀번호맞는지 확인
	@PostMapping(value = "/find/myPage/MachingPw")
	public String machingPw(Model model, Principal principal, @RequestParam("password") String password) {
		 
		Member member = memberService.findMember(principal.getName());
		
		if(passwordEncoder.matches(password, member.getPassword())){
			return "member/memberChaingePw";
		}else {
			model.addAttribute("errorMessage", "비밀번호가 다릅니다!");
			return "member/memberMachingPw";
		}
	}
}
