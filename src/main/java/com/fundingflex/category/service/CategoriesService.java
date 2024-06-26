package com.fundingflex.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;
import com.fundingflex.mybatis.mapper.category.CategoriesMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

	private final CategoriesMapper categoriesRepository;
	
	
	// 카테고리 조회
	public List<CategoriesDto> selectAllCategories() {
		return categoriesRepository.selectAllCategories();
	}
	
	// 카테고리 조회
	public CategoriesDto selectCategoriesById(Long categoryId) {
		return categoriesRepository.selectCategoryById(categoryId);
	}

}
