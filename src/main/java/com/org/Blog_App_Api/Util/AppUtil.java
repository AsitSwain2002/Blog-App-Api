package com.org.Blog_App_Api.Util;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class AppUtil {

	@Bean
	public ObjectMapper objMapper() {
		return new ObjectMapper();
	}

	public static String getUrl(HttpServletRequest url) {

		String baseUrl = url.getRequestURL().toString();
		String requestURI = url.getRequestURI();
		return baseUrl.replace(requestURI, "");
	}
}
