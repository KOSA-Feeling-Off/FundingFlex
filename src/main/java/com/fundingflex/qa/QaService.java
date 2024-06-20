package com.fundingflex.qa;

import java.util.Date;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QaService {

	private final QaRepository qaRepository;
	
	public void create(String title, String content) {

		Long userId = (long) 10;
		
        QaDTO qaDTO = new QaDTO();
        qaDTO.setTitle(title);
        qaDTO.setContent(content);
        qaDTO.setMembersUserId(userId);
        qaDTO.setCreatedAt(new Date());
        qaDTO.setIsDeleted("N");
        qaDTO.setReply(" ");

        this.qaRepository.save(qaDTO);
	}
	
    public Page<QaDTO> getUserQuestions(Long userId, PageRequest pageRequest) {
        return qaRepository.findByMembersUserId(userId, pageRequest);
    }

    public QaDTO getQuestionDetail(Long id) {
        return qaRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
    }
    
    public Page<QaDTO> getUserQuestionsByUserId(Long userId, Pageable pageable) {
        return qaRepository.findByAdminUserIdOrMembersUserId(userId, userId, pageable);
    }
}
