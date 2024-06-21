package com.fundingflex.qa.fqa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FqaRepository extends JpaRepository<FqaDTO, Long> {
    // 추가적인 Fqa 관련 메서드가 필요하다면 여기에 추가 가능
	 List<FqaDTO> findByTitleContainingIgnoreCase(String title);
	 List<FqaDTO> findByContentContainingIgnoreCase(String content);
	 List<FqaDTO> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}