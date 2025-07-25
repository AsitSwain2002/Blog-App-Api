package com.org.Blog_App_Api.Util;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppUtil {

	@Bean
	public ObjectMapper objMapper() {
		return new ObjectMapper();
	}
}
