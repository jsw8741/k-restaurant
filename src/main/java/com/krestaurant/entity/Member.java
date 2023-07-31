package com.krestaurant.entity;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.krestaurant.constant.Role;
import com.krestaurant.dto.MemberFormDto;
import com.krestaurant.dto.MemberModifyDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name="member") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Member {

	@Id
	@Column(name="member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 255)
	private String name;

	@Column(unique = true, length = 255)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String phone; 
	
	@Column(nullable = false, length = 255)
	private String address;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setPassword(password);
		member.setPhone(memberFormDto.getPhone());
		member.setAddress(memberFormDto.getAddress());
		member.setRole(Role.USER);
		
		return member;
	}
	
	public static Member createAdmin(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setPassword(password);
		member.setPhone(memberFormDto.getPhone());
		member.setAddress(memberFormDto.getAddress());
		member.setRole(Role.ADMIN);
		
		return member;
	}
	
	public static Member createMaster(PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode("12345678");
		
		Member member = new Member();
		
		member.setName("master");
		member.setEmail("master@naver.com");
		member.setPassword(password);
		member.setPhone("010-9999-9999");
		member.setAddress("서울시");
		member.setRole(Role.ADMIN);
		
		return member;
	}
	
	public void updateMember(MemberModifyDto memberModifyDto) {
		this.name = memberModifyDto.getName();
		this.phone = memberModifyDto.getPhone();
		this.address = memberModifyDto.getAddress();
	}
	
	
}
