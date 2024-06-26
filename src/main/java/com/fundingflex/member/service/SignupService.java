package com.fundingflex.member.service;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.member.repository.MembersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupService {

	private final MembersRepository membersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    

	// 회원가입
	public Members signup(MemberResisterForm resisterForm) {
		System.out.println("회원가입 시작!");
		Members newMembers = Members.builder()
				.email(resisterForm.getEmail())
				.nickname(resisterForm.getNickname())
				.password(passwordEncoder.encode(resisterForm.getPassword1()))
				.createdAt(new Date())
				.createdBy(resisterForm.getNickname())
				.role("USER")
				.build();
			return membersRepository.save(newMembers);
	}

	// 이메일 중복검사
	public boolean existsByEmail(String email) {
		return membersRepository.existsByEmail(email);
	}

	// 닉네임 중복검사
	public boolean existsByNickname(String nickname) {
		return membersRepository.existsByNickname(nickname);
	}

}
