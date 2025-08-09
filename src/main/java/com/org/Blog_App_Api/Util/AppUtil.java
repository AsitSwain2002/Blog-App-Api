package com.org.Blog_App_Api.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.securityConfig.AuthUser;

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

	public static Users getLoggedUser() {
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return authUser.getUser();
	}
}
