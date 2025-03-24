package com.org.Blog_App_Api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/save")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
		boolean saveCategory = categoryService.saveCategory(categoryDto);
		if (saveCategory) {
			return ResponseBuilder.withMessageNoData("Saved Successfully", HttpStatus.CREATED);
		} else {
			return ResponseBuilder.withErrorMessage("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
