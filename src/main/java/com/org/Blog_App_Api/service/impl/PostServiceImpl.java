package com.org.Blog_App_Api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Blog_App_Api.ExceptionHandler.ResourceNotFoundException;
import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.dto.FevoritePostDto;
import com.org.Blog_App_Api.dto.PostDto;
import com.org.Blog_App_Api.model.FevoritePost;
import com.org.Blog_App_Api.model.FileDetails;
import com.org.Blog_App_Api.model.Post;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.CategoryRepo;
import com.org.Blog_App_Api.repo.FevoriteRepo;
import com.org.Blog_App_Api.repo.FileRepo;
import com.org.Blog_App_Api.repo.PostRepo;
import com.org.Blog_App_Api.repo.UserRepo;
import com.org.Blog_App_Api.service.PostService;
import com.org.Blog_App_Api.validation.PostValidation;

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

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private FevoriteRepo fevoriteRepo;

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
		List<Post> findAllByCategory = postRepo.findAllByCategoryId(categoryId);
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
		int userId = AppUtil.getLoggedUser().getId();
		Post post = postRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + id + "Not Found"));
		if (post.getCreatedBy() != userId) {
			throw new IllegalArgumentException("Acess Denied");
		}
		post.setDeleted(true);
		post.setDeletedOn(new Date());
		postRepo.save(post);
	}

	@Override
	public List<PostDto> recycleBinPosts() {
		int userId = AppUtil.getLoggedUser().getId();
		Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		List<Post> findAllByDeletedFalse = postRepo.findAllByCreatedByAndDeletedTure(userId);
		return findAllByDeletedFalse.stream().map(ele -> mapper.map(ele, PostDto.class)).collect(Collectors.toList());
	}

	@Override
	public void fevoritePost(int postId) {
		int userId = AppUtil.getLoggedUser().getId();
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + postId + "Not Found"));
		Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		FevoritePost favPost = FevoritePost.builder().usersId(userId).post(post).build();
		fevoriteRepo.save(favPost);
	}

	@Override
	public List<FevoritePostDto> fevoritePost() {

		int userId = AppUtil.getLoggedUser().getId();
		Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

		List<FevoritePost> findAllByUsersId = fevoriteRepo.findAllByUsersId(userId);
		return findAllByUsersId.stream().map(ele -> mapper.map(ele, FevoritePostDto.class))
				.collect(Collectors.toList());

	}

	@Override
	public void unFevoritePost(int postId) {
		int userId = AppUtil.getLoggedUser().getId();
		Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + postId + " Not Found"));
		FevoritePost favPost = fevoriteRepo.findByPostId(postId);
		if (favPost == null) {
			throw new ResourceNotFoundException("Post Not Found");
		}
		fevoriteRepo.delete(favPost);
	}

	// remove post from recycle Bin
	@Override
	public void removePostFromRecycleBin(int postId) {
		int userId = AppUtil.getLoggedUser().getId();
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with Id  " + postId + "Not Found"));
		if (post.getCreatedBy() != userId) {
			throw new IllegalArgumentException("Acess Denied");
		}
		if (!post.isDeleted()) {
			throw new IllegalArgumentException("Acess Denied");
		}
		post.setDeleted(false);
		post.setDeletedOn(null);
		postRepo.save(post);

	}

	// remove all post from recycle Bin
	@Override
	public void removeAllRecycleBinPost() {

		int userId = AppUtil.getLoggedUser().getId();
		List<Post> post = postRepo.findAllByCreatedBy(userId);
		post.forEach(p -> {
			p.setDeleted(false);
			p.setDeletedOn(null);
		});

		postRepo.saveAll(post);
	}

}
