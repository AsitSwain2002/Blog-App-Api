package com.org.Blog_App_Api.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.dto.CategoryDto;
import com.org.Blog_App_Api.model.Category;
import com.org.Blog_App_Api.repo.CategoryRepo;
import com.org.Blog_App_Api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper mapper;
	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public boolean saveCategory(CategoryDto categoryDto) {
		Category category = mapper.map(categoryDto, Category.class);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category save = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}

}
