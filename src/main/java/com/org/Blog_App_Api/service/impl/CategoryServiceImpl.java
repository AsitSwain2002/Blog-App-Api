package com.org.Blog_App_Api.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.AlreadyExists;
import com.org.Blog_App_Api.ExceptionHandler.ResourceNotFoundException;
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

		// Name Already Present or not Check
		categoryAlreadyPrsent(category.getName());
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category save = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}
     
	private void categoryAlreadyPrsent(String name) {
		boolean existsByNameAndIsDeletedFalse = categoryRepo.existsByNameAndIsDeletedFalse(name);
		if (existsByNameAndIsDeletedFalse) {
			throw new AlreadyExists(name + " Already Present");
		}
	}

	@Override
	public CategoryDto findCategoryById(Integer id) {
		Category category = categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + id + "' Not Found"));

		return mapper.map(category, CategoryDto.class);
	}

}
