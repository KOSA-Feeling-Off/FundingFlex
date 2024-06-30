package com.fundingflex.mybatis.mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fundingflex.category.domain.entity.Categories;


@Mapper
public interface CategoriesMapper {
	
	List<Categories> selectAllCategories();

	Categories selectCategoryById(Long categoryId);

	int existsById(Long categoryId);
}
