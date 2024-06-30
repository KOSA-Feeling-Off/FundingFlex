package com.fundingflex.mybatis.mapper.funding;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.funding.domain.entity.Images;


@Mapper
public interface ImagesMapper {
   
   // 이미지 조회
   List<Images> findByfundingsId(Long fundingId);

   
   // 이미지 저장
   int insertFundingsImages(@Param("imageList") List<Images> imageList);

}
