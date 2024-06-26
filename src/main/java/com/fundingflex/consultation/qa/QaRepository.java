package com.fundingflex.consultation.qa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;

public interface QaRepository extends JpaRepository<QaDTO, Long>{

	Page<QaDTO> findByMembersUserId(Long membersUserId, Pageable pageable);
	Page<QaDTO> findByMembersUserIdOrderByCounsulIdDesc(Long membersUserId, Pageable pageable);
}
