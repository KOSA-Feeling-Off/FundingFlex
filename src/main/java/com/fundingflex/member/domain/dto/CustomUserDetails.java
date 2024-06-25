package com.fundingflex.member.domain.dto;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fundingflex.member.domain.entity.Members;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final Members members; // 인증된 사용자 정보를 저장하는 UserEntity객체

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 사용자 권한을 반환합니다.
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				// UserEntity에서 사용자 권한을 가져와 반환합니다.
				return members.getRole();
			}
		});
		return collection;
	}

	@Override
	public String getPassword() {
		// UserEntity에서 비밀번호를 가져와 반환합니다.
		return members.getPassword();
	}

	@Override
	public String getUsername() {
		// UserEntity에서 사용자 이름을 가져와 반환합니다.
		return members.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// 계정이 만료되지 않았음
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// 계정이 잠기지 않았음
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 자격 증명이 만료되지 않았음
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 계정이 활성화되었음
		return true;
	}
}