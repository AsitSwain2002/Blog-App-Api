package com.org.Blog_App_Api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Blog_App_Api.ExceptionHandler.ResourceNotFoundException;
import com.org.Blog_App_Api.dto.FileDetailsDto;
import com.org.Blog_App_Api.dto.PostDto;
import com.org.Blog_App_Api.model.Category;
import com.org.Blog_App_Api.model.FileDetails;
import com.org.Blog_App_Api.model.Post;
import com.org.Blog_App_Api.repo.CategoryRepo;
import com.org.Blog_App_Api.repo.FileRepo;
import com.org.Blog_App_Api.repo.PostRepo;
import com.org.Blog_App_Api.service.PostService;
import com.org.Blog_App_Api.validation.PostValidation;

import ch.qos.logback.core.util.FileUtil;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ModelMapper mapper;

	@Value("${file.upload.path}")
	private String uploadFolderName;

	@Autowired
	private FileRepo fileRepo;

	@Autowired
	private PostValidation postValidation;

	@Override
	public boolean createrPost(String postReq, MultipartFile file) throws IOException {

		// String convert to dto file
		ObjectMapper ob = new ObjectMapper();
		PostDto postDto = ob.readValue(postReq, PostDto.class);

		Post post = mapper.map(postDto, Post.class);
		// validation here
		postValidation.postValidate(postDto);

		// Update post
		if (postDto.getId() != null) {
			postUpdate(post, file);
		}
		// File Upload
		FileDetails fileDetails = saveFile(file);
		if (!ObjectUtils.isEmpty(fileDetails)) {
			post.setFileDetails(fileDetails);
		} else {
			if (ObjectUtils.isEmpty(postDto.getId())) {
				post.setFileDetails(null);
			}
		}
		// Category Exist or not
		categoryExist(postDto.getCategory().getId());
		Post save = postRepo.save(post);
		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}

	// Find All post By category
	@Override
	public List<PostDto> findpostByCategory(int categoryId) {
		categoryExist(categoryId);
		List<Post> findAllByCategory = postRepo.findAllByCategory(categoryId);
		return findAllByCategory.stream().map(e -> mapper.map(e, PostDto.class)).collect(Collectors.toList());

	}

	private void categoryExist(int id) {
		categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with Id  " + id + " Not Found"));
	}

	private void postUpdate(Post postDto, MultipartFile file) {
		Post post = postRepo.findById(postDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + postDto.getId() + " Not Found"));
		if (ObjectUtils.isEmpty(file)) {
			postDto.setFileDetails(post.getFileDetails());
		}
	}

	private FileDetails saveFile(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && file != null) {

			String originalFilename = file.getOriginalFilename(); // get the original file name
			String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();

			List<String> supportExtension = Arrays.asList("png", "jpg", "jpeg");
			if (!supportExtension.contains(extension)) {
				throw new IllegalArgumentException("png,jpg,jpeg is Supported");
			}
			String randomName = UUID.randomUUID().toString(); // random number generate here
			String uploadFileName = randomName + "." + extension;

			String displayFileName = getdisplayFileName(originalFilename);
			long fileSize = file.getSize();

			File saveFile = new File(uploadFolderName);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			String storedPath = uploadFolderName.concat(uploadFileName);

			long copy = Files.copy(file.getInputStream(), Path.of(storedPath));
			if (copy != 0) {
				FileDetails fileDetails = new FileDetails();
				fileDetails.setDisplayFileName(displayFileName);
				fileDetails.setFileSize(fileSize);
				fileDetails.setOriginalFileName(originalFilename);
				fileDetails.setPath(storedPath);
				fileDetails.setUploadFileName(uploadFileName);
				return fileRepo.save(fileDetails);
			}
		}
		return null;
	}

	private String getdisplayFileName(String originalFilename) {
		String displayName = "";
		String baseName = FilenameUtils.getBaseName(originalFilename);
		String extension = FilenameUtils.getExtension(originalFilename);
		if (baseName.length() > 7) {
			String substring = baseName.substring(0, 8);
			displayName = substring;
		}
		return displayName + "." + extension;
	}

	@Override
	public List<PostDto> fetchAllPost() {
		List<Post> findAllByIsDeletedFalse = postRepo.findAllByDeletedFalse();
		List<PostDto> collect = findAllByIsDeletedFalse.stream().map(e -> mapper.map(e, PostDto.class))
				.collect(Collectors.toList());
		return collect;
	}

	@Override
	public PostDto findpostById(int id) {

		Post post = postRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + id + "Not Found"));
		if (post.isDeleted() == false) {
			return mapper.map(post, PostDto.class);
		} else {
			return null;
		}
	}

	@Override
	public void deletePost(int id) {
		Post post = postRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + id + "Not Found"));
		post.setDeleted(true);
		postRepo.save(post);
	}

}
