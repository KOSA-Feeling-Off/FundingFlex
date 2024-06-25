package com.fundingflex.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fundingflex.member.domain.entity.Members;


public interface MembersRepository extends JpaRepository<Members, Long> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);
	
	//username을 받아 DB 테이블에서 회원을 조회하는 메서드 작성
	Members findByEmail(String username);
}
