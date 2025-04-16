package com.org.Blog_App_Api.service;

import java.util.List;

import com.org.Blog_App_Api.dto.PostDto;

public interface PostService {

	public boolean createrPost(PostDto postDto);

	public List<PostDto> fetchAllPost();

	public PostDto findpostById(int id);
	
	public void deletePost(int id);
	
}
