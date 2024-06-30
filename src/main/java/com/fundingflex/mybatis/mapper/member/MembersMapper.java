package com.fundingflex.mybatis.mapper.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.member.domain.entity.Members;

@Mapper
public interface MembersMapper {
	void insertMember(Members member);

    Members findByEmail(@Param("email") String email);

	// 이메일 중복 확인
	boolean existsByEmail(String email);

	// 닉네임 중복 확인
	boolean existsByNickname(String nickname);
}
