package com.fundingflex.mybatis.mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fundingflex.category.domain.dto.CategoriesDTO;

@Mapper
public interface CategoriesMapper {
    List<CategoriesDTO> selectAllCategories();
    CategoriesDTO selectCategoryById(Long categoryId);
    int existsById(Long categoryId);
}