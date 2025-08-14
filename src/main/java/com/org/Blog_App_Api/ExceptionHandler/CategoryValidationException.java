package com.org.Blog_App_Api.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class CategoryValidationException extends RuntimeException {

	Map<String, Object> error = new LinkedHashMap<String, Object>();

	public CategoryValidationException(Map<String, Object> error) {
		super("Validation Failed");
		this.error = error;
	}

}
