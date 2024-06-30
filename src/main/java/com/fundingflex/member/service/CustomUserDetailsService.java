package com.fundingflex.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.mybatis.mapper.member.MembersMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MembersMapper membersMapper;

	 @Transactional(readOnly = true)
	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        // 이메일을 사용하여 사용자 조회
	        Members member = membersMapper.findByEmail(email);
	        if (member == null) {
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }

	        // 조회된 사용자 정보로 UserDetails 객체 생성
//	        return new User(
//	            member.getEmail(), 
//	            member.getPassword(),
//	            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole())));
		    return new CustomUserDetails(member);
	    }
}
