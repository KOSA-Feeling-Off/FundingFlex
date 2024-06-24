package com.fundingflex.qa;

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

import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.common.enums.Enums;
import com.fundingflex.funding.domain.entity.Images;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QaService {

	private final QaRepository qaRepository;
	
	// 이미지 파일 경로
	private final Path qaImgPath = Paths.get("src/main/resources/static/images/qa ");
	
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
	    List<Images> imageList = new ArrayList<>();
	    try {
	        for (MultipartFile file : images) {
	        	
	            if (!file.isEmpty()) {
	                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                Files.copy(file.getInputStream(), this.fundingImgPath.resolve(fileName));

	                Images newImage = Images.builder()
	                        .imageUrl(fileName)
	                        .fundings(newFundings)
	                        .seq(idx++)
	                        .isDeleted(DeleteFlagEnum.N)
	                        .build();

	                imageList.add(newImage);
	            }
	        }
	        
	    } catch (IOException ex) {
	        throw new RuntimeException("이미지 처리 실패: " + ex.getMessage());
	    }
	}
	
    public Page<QaDTO> getUserQuestions(Long userId, PageRequest pageRequest) {
        return qaRepository.findByMembersUserId(userId, pageRequest);
    }

    public QaDTO getQuestionDetail(Long id) {
        return qaRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
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