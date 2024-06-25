package com.fundingflex.qa.images;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundingflex.qa.QaDTO;

public interface QaImagesRepository extends JpaRepository<QaImagesDTO, Long>{
	List<QaImagesDTO> findByConsultations(QaDTO consultations);
}
