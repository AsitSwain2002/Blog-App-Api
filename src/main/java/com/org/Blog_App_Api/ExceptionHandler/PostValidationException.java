package com.org.Blog_App_Api.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class PostValidationException extends RuntimeException {

	Map<String, Object> errors = new LinkedHashMap<String, Object>();

	public PostValidationException(Map<String, Object> errors) {
		super("validation Failed");
		this.errors = errors;
	}

	public Map<String, Object> getErrors() {
		return errors;
	}

    
}
