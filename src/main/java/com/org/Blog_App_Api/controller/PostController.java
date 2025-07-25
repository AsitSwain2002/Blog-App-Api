package com.org.Blog_App_Api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.dto.PostDto;
import com.org.Blog_App_Api.service.PostService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/post")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

	@Autowired
	private PostService postService;

	@PostMapping("/savePost")
	public ResponseEntity<?> savePost(@RequestParam String postDto, @RequestParam("file") MultipartFile file)
			throws IOException {
		boolean createrPost = postService.createrPost(postDto, file);
		if (createrPost) {
			return ResponseBuilder.withMessageNoData("post Saved Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Intername Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all-post")
	public ResponseEntity<?> fetchAllPost(HttpServletRequest req) {
		List<PostDto> fetchAllPost = postService.fetchAllPost();
		if (ObjectUtils.isEmpty(fetchAllPost)) {
			return ResponseBuilder.withMessageNoData("No data Present", HttpStatus.NO_CONTENT);
		} else {
			return ResponseBuilder.withMessage("fetched", fetchAllPost, HttpStatus.OK);
		}
	}

	@GetMapping("/find-post/{id}")
	public ResponseEntity<?> findPost(@PathVariable int id) {
		PostDto findpostById = postService.findpostById(id);
		if (findpostById == null) {
			return ResponseBuilder.withMessageNoData("Post Not Found", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessage("Fetched Successfully", findpostById, HttpStatus.OK);
		}

	}

	@GetMapping("/find-all-post/{categoryId}")
	public ResponseEntity<?> findAllPostByCategory(@PathVariable int categoryId) {
		List<PostDto> allPostByCategory = postService.findpostByCategory(categoryId);
		if (CollectionUtils.isEmpty(allPostByCategory)) {
			return ResponseBuilder.withMessageNoData("Post Not Found", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessage("Fetched Successfully", allPostByCategory, HttpStatus.OK);
		}

	}

	@DeleteMapping("/delete-post/{id}")
	public ResponseEntity<?> deletePost(@PathVariable int id) {
		postService.deletePost(id);
		return ResponseBuilder.withMessageNoData("Post Delete SuccessFully", HttpStatus.NO_CONTENT);
	}

}
