package com.org.Blog_App_Api.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.org.Blog_App_Api.dto.LoginRequest;
import com.org.Blog_App_Api.dto.LoginResponse;

public interface AuthService {

	public boolean registerUser(String userDto, MultipartFile file,String url) throws IOException, Exception;

	public LoginResponse login(LoginRequest loginRequest);
}
