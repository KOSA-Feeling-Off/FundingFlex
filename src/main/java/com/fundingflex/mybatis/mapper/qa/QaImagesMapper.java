package com.fundingflex.mybatis.mapper.qa;

import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QaImagesMapper {

    List<QaImagesDTO> findByConsultations(@Param("counsulId") Long counsulId);

    void save(QaImagesDTO qaImagesDTO);

    void saveAll(List<QaImagesDTO> images);

    void deleteById(@Param("qaImageId") Long qaImageId);
}
