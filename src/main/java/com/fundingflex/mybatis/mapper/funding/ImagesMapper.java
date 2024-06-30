package com.fundingflex.mybatis.mapper.funding;

import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.entity.Images;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ImagesMapper {

	// 이미지 조회
	List<Images> findByfundingsId(Long fundingId);


	// 이미지 저장
	int insertFundingsImages(@Param("imageList") List<Images> imageList);


	// 이미지 IsDelete 수정
	int updateImageIsDelete(@Param("fundingId") Long fundingId,
		@Param("isDelete") DeleteFlagEnum isDelete);
}
