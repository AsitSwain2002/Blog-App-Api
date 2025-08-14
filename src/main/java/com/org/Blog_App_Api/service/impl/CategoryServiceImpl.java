package com.org.Blog_App_Api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.org.Blog_App_Api.validation.CategoryValidation;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper mapper;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private CategoryValidation categoryValidation;

	@Override
	public boolean saveCategory(CategoryDto categoryDto) {
		// validation

		categoryValidation.categoryValidation(categoryDto);
		Category category = mapper.map(categoryDto, Category.class);
		// update logic
		if (category.getId() != null) {
			updateCategory(category);
		} else {
			// Name Already Present or not Check
			categoryAlreadyPrsent(category.getName());
		}
		if (category.getId() == 0) {
			// Name Already Present or not Check
			categoryAlreadyPrsent(category.getName());
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
		category.setCatDeleted(categoryIn.isCatDeleted());
	}

	private void categoryAlreadyPrsent(String name) {
		boolean existsByNameAndIsDeletedFalse = categoryRepo.existsByNameAndCatDeletedFalse(name);
		if (existsByNameAndIsDeletedFalse) {
			throw new AlreadyExists(name + " Already Present");
		}
	}

	@Override
	public CategoryDto findCategoryById(Integer id) {
		Category category = categoryRepo.findByIdAndCatDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + id + "' Not Found"));

		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> findAllCategory() {
		List<Category> findAllByisDeletedFalse = categoryRepo.findByCatDeletedFalse();
		return findAllByisDeletedFalse.stream().map(e -> mapper.map(e, CategoryDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteCategory(Integer id) {
		Category category = categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + id + "' Not Found"));
		category.setCatDeleted(true);
		categoryRepo.save(category);
	}

}
