package com.fundingflex.consultation.qa.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.common.enums.Enums;
import com.fundingflex.consultation.qa.QaImagesRepository;
import com.fundingflex.consultation.qa.QaRepository;
import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QaService {

	private final QaRepository qaRepository;
	private final QaImagesRepository qaImagesRepository;
	
	// 이미지 파일 경로
	private final Path qaImgPath = Paths.get("src/main/resources/static/images/qa");
	
	public void qaCreate(String title, String content, MultipartFile[] images) {

		// 폴더 경로 생성
	    if (!Files.exists(qaImgPath)) {
	    	
	        try {
	            Files.createDirectories(qaImgPath);
	            
	        } catch (IOException ex) {
	            throw new RuntimeException("폴더 생성 실패: " + ex.getMessage());
	        }
	    }
		
		Long userId = (long) 10;
		
        QaDTO qaDTO = new QaDTO();
        qaDTO.setTitle(title);
        qaDTO.setContent(content);
        qaDTO.setMembersUserId(userId);
        qaDTO.setCreatedAt(new Date());
        qaDTO.setIsReply(Enums.Admin.N.name());
        qaDTO.setReply(" ");
        
        this.qaRepository.save(qaDTO);
        
	    // 이미지 처리 및 Images 객체 생성
	    int idx = 1;		// 파일 순서
	    List<QaImagesDTO> imageList = new ArrayList<>();
	    try {
	        for (MultipartFile file : images) {
	        	
	            if (!file.isEmpty()) {
	                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                Files.copy(file.getInputStream(), this.qaImgPath.resolve(fileName));

	                QaImagesDTO qaImagesDTO = new QaImagesDTO();

	                qaImagesDTO.setConsultations(qaDTO);
	                qaImagesDTO.setQaImageUrl(fileName);
	                qaImagesDTO.setSeq(idx++);
	                qaImagesDTO.setCreatedAt(new Date());
	                
	                imageList.add(qaImagesDTO);
	            }
	        }
	        
	    } catch (IOException ex) {
	        throw new RuntimeException("이미지 처리 실패: " + ex.getMessage());
	    }
	    
	    qaRepository.save(qaDTO);
	    qaImagesRepository.saveAll(imageList);
	}
	
    public Page<QaDTO> getUserQuestions(Long userId, PageRequest pageRequest) {
        return qaRepository.findByMembersUserId(userId, pageRequest);
    }

//    public QaDTO getQuestionDetail(Long id) {
//        return qaRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
//    }

    public QaDTO getQuestionDetail(Long id) {
        QaDTO question = qaRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
        List<QaImagesDTO> images = qaImagesRepository.findByConsultations(question);
        question.setImages(images); // QaDTO에 이미지 리스트를 추가하는 setter를 만들어야 함
        return question;
    }
    
    public Page<QaDTO> findByMembersUserIdOrderByCounsulIdDesc(Long userId, Pageable pageable) {
        return qaRepository.findByMembersUserIdOrderByCounsulIdDesc(userId, pageable);
    }

	public void replyCreate(Long memberId, Long adminId, String content) {
		QaDTO qaDTO = getQuestionDetail(memberId);
		qaDTO.setAdminUserId(adminId);
		qaDTO.setIsReply(Enums.Admin.Y.name());
		qaDTO.setReply(content);
		this.qaRepository.save(qaDTO);
	}
}
