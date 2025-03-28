package com.org.Blog_App_Api.service.impl;

import java.util.Date;
import java.util.List;

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

		// validation

		Category category = mapper.map(categoryDto, Category.class);
		// Name Already Present or not Check
		categoryAlreadyPrsent(category.getName());
		// update logic
		if (category.getId() != null) {
			updateCategory(category);
		}
		Category save = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}

	// update Category method
	private void updateCategory(Category categoryIn) {

		int catId = categoryIn.getId();
		Category category = categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + catId + "' Not Found"));
		category.setName(categoryIn.getName());
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

	@Override
	public List<CategoryDto> findAllCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCategory(Integer id) {
		// TODO Auto-generated method stub

	}

}
