package com.fundingflex.mybatismapper.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;

@Repository
@Mapper
public interface CategoriesRepository {
	
	List<CategoriesDto> selectAllCategories();
}
