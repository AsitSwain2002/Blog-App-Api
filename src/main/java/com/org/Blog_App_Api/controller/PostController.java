package com.org.Blog_App_Api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.dto.PostDto;
import com.org.Blog_App_Api.service.PostService;

@RestController
@RequestMapping("api/v1/post")
public class PostController {

	@Autowired
	private PostService postService;

	@PostMapping("/savePost")
	public ResponseEntity<?> savePost(@RequestParam String postDto, @RequestParam(required = false) MultipartFile file)
			throws IOException {
		boolean createrPost = postService.createrPost(postDto, file);
		if (createrPost) {
			return ResponseBuilder.withMessageNoData("post Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withErrorMessage("Intername Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all-post")
	public ResponseEntity<?> fetchAllPost() {
		List<PostDto> fetchAllPost = postService.fetchAllPost();
		if (ObjectUtils.isEmpty(fetchAllPost)) {
			return ResponseBuilder.withMessageNoData("No data Present", HttpStatus.NO_CONTENT);
		} else {
			return ResponseBuilder.withMessage("fetched", fetchAllPost, HttpStatus.OK);
		}
	}

}
