package com.fundingflex.member.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.mybatis.mapper.member.MembersMapper;

@Service
public class SignupService {

    private final PasswordEncoder passwordEncoder;
	private final MembersMapper membersMapper;

	@Autowired
    public SignupService(MembersMapper membersMapper, PasswordEncoder passwordEncoder) {
        this.membersMapper = membersMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Members signup(MemberResisterForm resisterForm) {
    	
    	String encodedPassword = passwordEncoder.encode(resisterForm.getPassword1());
    	
        Members newMember = Members.builder()
                .email(resisterForm.getEmail())
                .nickname(resisterForm.getNickname())
                .password(encodedPassword)
                .createdAt(new Date())
                .createdBy(resisterForm.getNickname())
                .role("USER")
                .build();

        membersMapper.insertMember(newMember);
        return newMember;
    }
    
    public boolean existsByEmail(String email) {
        return membersMapper.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return membersMapper.existsByNickname(nickname);
    }  
}
