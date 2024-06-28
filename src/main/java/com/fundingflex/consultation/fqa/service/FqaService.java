//package com.fundingflex.consultation.fqa.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fundingflex.consultation.fqa.FqaRepository;
//import com.fundingflex.consultation.fqa.domain.dto.FqaDTO;
//import com.fundingflex.consultation.qa.domain.dto.QaDTO;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class FqaService {
//
//    private final FqaRepository fqaRepository;
//
//    @Autowired
//    public FqaService(FqaRepository fqaRepository) {
//        this.fqaRepository = fqaRepository;
//    }
//
//    public List<FqaDTO> getAllFqas() {
//        return fqaRepository.findAll();
//    }
//
//    public void saveFqa(FqaDTO fqa) {
//    	fqaRepository.save(fqa);
//    }
//
//    public FqaDTO getFqaById(Long id) {
//        return fqaRepository.findById(id)
//                             .orElseThrow(() -> new RuntimeException("Fqa not found with id: " + id));
//    }
//
//	public void create(Long adminId, String title, String content, String reply) {
//        FqaDTO fqaDTO = new FqaDTO();
//        fqaDTO.setTitle(title);
//        fqaDTO.setContent(content);
//        fqaDTO.setReply(reply);
//        fqaDTO.setAdminUserId(adminId);
//        fqaDTO.setCreatedAt(new Date());
//        
//        this.fqaRepository.save(fqaDTO);
//		
//	}
//	
//    public List<FqaDTO> findAll() {
//        return fqaRepository.findAll();
//    }
//	
//    public List<FqaDTO> findByTitle(String title) {
//        return fqaRepository.findByTitleContainingIgnoreCase(title);
//    }
//
//    public List<FqaDTO> findByContent(String content) {
//        return fqaRepository.findByContentContainingIgnoreCase(content);
//    }
//
//    public List<FqaDTO> findByTitleOrContent(String keyword) {
//        return fqaRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
//    }
//}

package com.fundingflex.consultation.fqa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundingflex.mybatis.mapper.fqa.FqaMapper;
import com.fundingflex.consultation.fqa.domain.dto.FqaDTO;

import java.util.Date;
import java.util.List;

@Service
public class FqaService {

    private final FqaMapper fqaMapper;

    @Autowired
    public FqaService(FqaMapper fqaMapper) {
        this.fqaMapper = fqaMapper;
    }

    public List<FqaDTO> getAllFqas() {
        return fqaMapper.findAll();
    }

    public void saveFqa(FqaDTO fqa) {
        fqaMapper.save(fqa);
    }

    public FqaDTO getFqaById(Long id) {
        return fqaMapper.findById(id);
    }

    public void create(Long adminId, String title, String content, String reply) {
        FqaDTO fqaDTO = new FqaDTO();
        fqaDTO.setTitle(title);
        fqaDTO.setContent(content);
        fqaDTO.setReply(reply);
        fqaDTO.setAdminUserId(adminId);
        fqaDTO.setCreatedAt(new Date());

        fqaMapper.save(fqaDTO);
    }

    public List<FqaDTO> findAll() {
        return fqaMapper.findAll();
    }

    public List<FqaDTO> findByTitle(String title) {
        return fqaMapper.findByTitleContainingIgnoreCase(title);
    }

    public List<FqaDTO> findByContent(String content) {
        return fqaMapper.findByContentContainingIgnoreCase(content);
    }

    public List<FqaDTO> findByTitleOrContent(String keyword) {
        return fqaMapper.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword);
    }
}
