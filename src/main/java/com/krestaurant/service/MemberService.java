package com.krestaurant.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krestaurant.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;

import com.krestaurant.dto.MemberFormDto;
import com.krestaurant.dto.MemberModifyDto;
import com.krestaurant.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{
	
	private final MemberRepository memberRepository;
	
	public Member saveMember(Member member) {
		validateDuplicateMember(member); // 이메일 중복체크
		Member saveMember = memberRepository.save(member); // insert
		return saveMember; //회원가입된 데이터를 칮는다.
	}
	
	// 이메일 중복 체크
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
	
	// 마이 페이지
	@Transactional(readOnly = true)
	public MemberModifyDto getMyPage(String email) {
		
		Member memberData = memberRepository.findByEmail(email);
		
		MemberModifyDto memberModifyDto = MemberModifyDto.of(memberData);
		return memberModifyDto;
	}
	
	// 내 정보 수정
	public Long updateMember(MemberModifyDto memberModifyDto, String email) throws Exception{
		Member member = memberRepository.findByEmail(email);
		
		member.updateMember(memberModifyDto);
		
		return member.getId();
	}
	
	// 탈퇴
	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
									    .orElseThrow(EntityNotFoundException::new);
		
		memberRepository.delete(member);
	}
	
	// 이메일로 회원 정보 찾기
	public Member findMember(String email) {
		Member member = memberRepository.findByEmail(email);
		return member;
	}
	
	// 비밀번호로 변경
	public Member updatePassword(Member member) {
		memberRepository.save(member);
		return member;
	}
	
	// 관리자 리스트
	public List<Member> getAdminList(){
		List<Member> adminList = memberRepository.getAdminList();
		
		return adminList;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 사용자가 입력한 email이 DB에있는지 쿼리문을 사용한다.
		Member member = memberRepository.findByEmail(email);
		
		if(member == null) {
			throw new UsernameNotFoundException(email);
		}
		 
		// 사용자가 있다면 DB에서 가져온 값으로 userDetails 객체를 만들어서 반환
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
}
