package com.org.Blog_App_Api.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {

	public boolean registerUser(String userDto, MultipartFile file) throws IOException;
}
