package com.org.Blog_App_Api.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.CategoryValidationException;
import com.org.Blog_App_Api.dto.CategoryDto;
import com.org.Blog_App_Api.model.Category;

@Component
public class CategoryValidation {

	Map<String, Object> error = new LinkedHashMap<String, Object>();

	public void categoryValidation(CategoryDto categoryDto) {
		if (ObjectUtils.isEmpty(categoryDto)) {
			error.put("Json error", "Json/Data can not be null");
		} else {
			if (categoryDto.getName().length() < 3) {
				error.put("name", "name is too short");
			}
			if (categoryDto.getName().length() > 20) {
				error.put("name", "name is too big");
			}
		}
		if (!ObjectUtils.isEmpty(error)) {
			throw new CategoryValidationException(error);
		}
	}
}
