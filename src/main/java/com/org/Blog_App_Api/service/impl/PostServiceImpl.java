package com.org.Blog_App_Api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.dto.PostDto;
import com.org.Blog_App_Api.model.Post;
import com.org.Blog_App_Api.repo.PostRepo;
import com.org.Blog_App_Api.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public boolean createrPost(PostDto postDto) {

		// validation here
		Post post = mapper.map(postDto, Post.class);
		Post save = postRepo.save(post);
		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}

	@Override
	public List<PostDto> fetchAllPost() {
		List<Post> findAllByIsDeletedFalse = postRepo.findAllByIsDeletedFalse();
		List<PostDto> collect = findAllByIsDeletedFalse.stream().map(e -> mapper.map(e, PostDto.class))
				.collect(Collectors.toList());
		return collect;
	}

	@Override
	public PostDto findpostById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePost(int id) {
		// TODO Auto-generated method stub

	}

}
