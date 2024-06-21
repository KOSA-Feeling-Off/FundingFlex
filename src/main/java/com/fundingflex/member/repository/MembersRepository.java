package com.fundingflex.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundingflex.member.dto.MembersDTO;

public interface MembersRepository extends JpaRepository<MembersDTO, Long> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<MembersDTO> findByEmail(String email);
}
