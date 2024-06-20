package com.fundingflex.qa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaRepository extends JpaRepository<QaDTO, Long>{

	Page<QaDTO> findByMembersUserId(Long membersUserId, Pageable pageable);
	 Page<QaDTO> findByAdminUserIdOrMembersUserId(Long adminUserId, Long membersUserId, Pageable pageable);
}
