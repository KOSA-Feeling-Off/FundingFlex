package com.fundingflex.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundingflex.category.domain.entity.Categories;
import com.fundingflex.mybatis.mapper.category.CategoriesMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

	private final CategoriesMapper categoriesMapper;
	
	
	// 카테고리 조회
	public List<Categories> selectAllCategories() {
		return categoriesMapper.selectAllCategories();
	}
	
	// 카테고리 조회
	public Categories selectCategoriesById(Long categoryId) {
        return categoriesMapper.selectCategoryById(categoryId);
    }

}
