package com.org.Blog_App_Api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.org.Blog_App_Api.dto.PostDto;

public interface PostService {

	public boolean createrPost(String postDto,MultipartFile file) throws JsonMappingException, JsonProcessingException, IOException;

	public List<PostDto> fetchAllPost();

	public PostDto findpostById(int id);
	
	public void deletePost(int id);
	
}
