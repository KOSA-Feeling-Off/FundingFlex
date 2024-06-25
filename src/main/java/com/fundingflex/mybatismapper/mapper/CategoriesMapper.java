package com.fundingflex.mybatismapper.mapper;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CategoriesMapper {
	
	List<CategoriesDto> selectAllCategories();

	CategoriesDto selectCategoryById(Long categoryId);

	int existsById(Long categoryId);
}
