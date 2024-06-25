package com.fundingflex.member.service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.member.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MembersRepository membersRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 사용자 이름으로 UserEntity를 검색합니다.
		// 반환 타입이 Optional이 아닌 경우, Optional로 래핑합니다.
		Members members = membersRepository.findByEmail(email);

		// userEntity를 Optional로 래핑한 후,
		// 존재하면 CustomUserDetails 객체로 변환하고,
		// 존재하지 않으면 UsernameNotFoundException 예외를 던집니다.
		return Optional.ofNullable(members).map(CustomUserDetails::new)
				// UserEntity를 CustomUserDetails 객체로 변환합니다.
				// =new CustomUserDetails(userData)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		// 존재하지 않으면 예외를 던집니다.
	}
}
