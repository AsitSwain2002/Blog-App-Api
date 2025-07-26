package com.org.Blog_App_Api.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class UsersValidationException extends RuntimeException {

	private Map<String, Object> error = new LinkedHashMap<String, Object>();

	public UsersValidationException(Map<String, Object> error) {
		this.error = error;
	}

}
