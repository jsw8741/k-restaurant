package com.krestaurant.dto;

import org.modelmapper.ModelMapper;

import com.krestaurant.constant.Role;
import com.krestaurant.entity.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberModifyDto {
	private Long id;
	
	@NotBlank(message = "이름은 필수입력 값입니다.")
	private String name;
	
	private String email;
	
	@NotEmpty(message = "전화번호는 필수입력 값입니다.")
	private String phone;
	
	@NotEmpty(message = "주소는 필수입력 값입니다.")
	private String address;
	
	private Role role;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//entity -> dto
	public static MemberModifyDto of(Member member) {
		return modelMapper.map(member, MemberModifyDto.class);
	}
}
