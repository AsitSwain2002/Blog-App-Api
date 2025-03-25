package com.org.Blog_App_Api.service;

import com.org.Blog_App_Api.dto.CategoryDto;

public interface CategoryService {

	public boolean saveCategory(CategoryDto categoryDto);

	public CategoryDto findCategoryById(Integer id);
}
