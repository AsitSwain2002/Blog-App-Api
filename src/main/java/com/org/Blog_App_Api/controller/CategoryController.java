package com.org.Blog_App_Api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.dto.CategoryDto;
import com.org.Blog_App_Api.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
		boolean saveCategory = categoryService.saveCategory(categoryDto);
		if (saveCategory) {
			return ResponseBuilder.withMessageNoData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withErrorMessage("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCategory/{id}")
	public ResponseEntity<?> findCategoyById(@PathVariable Integer id) {
		CategoryDto findCategoryById = categoryService.findCategoryById(id);
		if (ObjectUtils.isEmpty(findCategoryById)) {
			return ResponseBuilder.withErrorMessage("No Content Present", HttpStatus.NO_CONTENT);
		} else {
			return ResponseBuilder.withMessage("Fethed", findCategoryById, HttpStatus.OK);
		}
	}

	@GetMapping("/allCategory")
	public ResponseEntity<?> findAllCategory() {
		List<CategoryDto> findAllCategory = categoryService.findAllCategory();
		if (ObjectUtils.isEmpty(findAllCategory)) {
			return ResponseBuilder.withErrorMessage("No Content Present", HttpStatus.NO_CONTENT);
		} else {
			return ResponseBuilder.withMessage("Fethed", findAllCategory, HttpStatus.OK);
		}
	}

	@DeleteMapping("/deleteCategory/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
		categoryService.deleteCategory(id);
		return ResponseBuilder.withMessageNoData("Deleted", HttpStatus.NO_CONTENT);
	}
}
