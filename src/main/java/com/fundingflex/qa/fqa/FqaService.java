package com.fundingflex.qa.fqa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundingflex.qa.QaDTO;

import java.util.Date;
import java.util.List;

@Service
public class FqaService {

    private final FqaRepository fqaRepository;

    @Autowired
    public FqaService(FqaRepository fqaRepository) {
        this.fqaRepository = fqaRepository;
    }

    public List<FqaDTO> getAllFqas() {
        return fqaRepository.findAll();
    }

    public void saveFqa(FqaDTO fqa) {
    	fqaRepository.save(fqa);
    }

    public FqaDTO getFqaById(Long id) {
        return fqaRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("Fqa not found with id: " + id));
    }

	public void create(Long adminId, String title, String content, String reply) {
        FqaDTO fqaDTO = new FqaDTO();
        fqaDTO.setTitle(title);
        fqaDTO.setContent(content);
        fqaDTO.setReply(reply);
        fqaDTO.setAdminUserId(adminId);
        fqaDTO.setCreatedAt(new Date());
        
        this.fqaRepository.save(fqaDTO);
		
	}
	
    public List<FqaDTO> findAll() {
        return fqaRepository.findAll();
    }
	
    public List<FqaDTO> findByTitle(String title) {
        return fqaRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<FqaDTO> findByContent(String content) {
        return fqaRepository.findByContentContainingIgnoreCase(content);
    }

    public List<FqaDTO> findByTitleOrContent(String keyword) {
        return fqaRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }
}