package com.fundingflex.consultation.qa.service;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;
import com.fundingflex.mybatis.mapper.qa.QaMapper;
import com.fundingflex.mybatis.mapper.qa.QaImagesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QaService {

    private final QaMapper qaMapper;
    private final QaImagesMapper qaImagesMapper;

    private final Path qaImgPath = Paths.get("src/main/resources/static/images/qa");

    @Transactional
    public void qaCreate(Long userId, String title, String content, MultipartFile[] images) {

        if (!Files.exists(qaImgPath)) {
            try {
                Files.createDirectories(qaImgPath);
            } catch (IOException ex) {
                throw new RuntimeException("폴더 생성 실패: " + ex.getMessage());
            }
        }

        QaDTO qaDTO = new QaDTO();
        qaDTO.setTitle(title);
        qaDTO.setContent(content);
        qaDTO.setMembersUserId(userId);
        qaDTO.setCreatedAt(new Date());
        qaDTO.setIsReply("N");
        qaDTO.setReply(" ");
        qaDTO.setAdminUserId(null);
        
        System.out.println("Test_QaDTO -  저장 시작");
        
        qaMapper.save(qaDTO);
        
        System.out.println("Test_QaDTO -  저장 성공");
        
        qaDTO = qaMapper.findLastByMembersUserId(userId);
        
        // 디버깅: createdAt 값 확인
        System.out.println("createdAt: " + qaDTO.getCreatedAt());

        int idx = 1;
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

        if(imageList.size() != 0) {
        	System.out.println("imageList - 갯수는 : " + imageList.size());
        	
        	for(int i = 0; i < imageList.size(); ++i) {
        		qaImagesMapper.save(imageList.get(i));
        	}
        	
        	//qaImagesMapper.saveAll(imageList);        	
        }
    }

    public List<QaDTO> getUserQuestions(Long userId, int page, int size) {
        int offset = page * size;
        return qaMapper.findByMembersUserId(userId, offset + 1, offset + size);
    }

    public int countUserQuestions(Long userId) {
        return qaMapper.countByMembersUserId(userId);
    }

    public QaDTO getQuestionDetail(Long id) {
        QaDTO question = qaMapper.findById(id);
        List<QaImagesDTO> images = qaImagesMapper.findByConsultations(question.getCounsulId());
        question.setImages(images);
        return question;
    }

    public void replyCreate(Long memberId, Long adminId, String content) {
        QaDTO qaDTO = getQuestionDetail(memberId);
        qaDTO.setAdminUserId(adminId);
        qaDTO.setIsReply("Y");
        qaDTO.setReply(content);
        qaMapper.update(qaDTO);
    }

	public List<QaDTO> getUQuestions(int page, int size) {
		int offset = page * size;
        return qaMapper.findByAllQuestions(offset + 1, offset + size);
	}
}
