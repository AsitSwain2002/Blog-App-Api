package com.org.Blog_App_Api.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.PostValidationException;
import com.org.Blog_App_Api.dto.PostDto;

@Service
public class PostValidation {

	public void postValidate(PostDto postDto) {
		Map<String, Object> error = new LinkedHashMap<String, Object>();
		if (ObjectUtils.isEmpty(postDto)) {
			throw new IllegalArgumentException("Post data Can not be null");
		} else {
			if (postDto.getTitle().length() < 10) {
				error.put("title", "Title length must greater than 10");
			}
			if (postDto.getTitle().length() > 400) {
				error.put("title", "Title length must less than 400");
			}
			if (postDto.getContent().length() < 100) {
				error.put("content", "content length must greater than 100");
			}
			if (postDto.getContent().length() > 2500) {
				error.put("content", "content length must less than 2500");
			}
		}
		if (!ObjectUtils.isEmpty(error)) {
			throw new PostValidationException(error);
		}
	}
}
