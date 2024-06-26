package com.fundingflex.consultation.qa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;

public interface QaImagesRepository extends JpaRepository<QaImagesDTO, Long>{
	List<QaImagesDTO> findByConsultations(QaDTO consultations);
}
