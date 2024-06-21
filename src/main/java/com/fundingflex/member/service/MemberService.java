package com.fundingflex.member.service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundingflex.member.dto.MembersDTO;
import com.fundingflex.member.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
	
	private final MembersRepository membersRepository;
	private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위함
	
	
	public MembersDTO signup(String email, String nickname, String password) {

		MembersDTO members = new MembersDTO();
		members.setEmail(email);
		members.setNickname(nickname);
		members.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
		members.setCreatedAt(new Date()); // 회원가입 시점 설정
		members.setCreatedBy(nickname); // 회원가입한 사람의 닉네임 설정

		return membersRepository.save(members);
	}

	// 이메일 중복검사
	public boolean existsByEmail(String email) {
		return membersRepository.existsByEmail(email);
	}

	// 닉네임 중복검사
	public boolean existsByNickname(String nickname) {
		return membersRepository.existsByNickname(nickname);
	}
	
	
	
	// login
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<MembersDTO> memberDTO = membersRepository.findByEmail(email);

		if (memberDTO.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		MembersDTO membersDTO = memberDTO.get();
		return new User(membersDTO.getEmail(), membersDTO.getPassword(), Collections.emptyList());
	}
}
