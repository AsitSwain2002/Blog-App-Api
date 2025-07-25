package com.org.Blog_App_Api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Blog_App_Api.model.FileDetails;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.FileRepo;
import com.org.Blog_App_Api.repo.UserRepo;
import com.org.Blog_App_Api.service.UserService;

@Service
public class UserSerciceImpl implements UserService {

	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private FileRepo fileRepo;

	@Value("${file.upload.path}")
	private String folderName;

	@Override
	public boolean registerUser(String userDto, MultipartFile file) throws IOException {

		Users user = objMapper.readValue(userDto, Users.class);

		// user Validation

		// save User
		FileDetails saveFile = saveFile(file);
		if (!ObjectUtils.isEmpty(saveFile)) {
			user.setFileDetails(saveFile);
		}
		Users save = userRepo.save(user);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	private FileDetails saveFile(MultipartFile file) throws IOException {

		if (!ObjectUtils.isEmpty(file) && file != null) {
			String originalFilename = file.getOriginalFilename().toLowerCase();
			String extension = FilenameUtils.getExtension(originalFilename);

			List<String> allowedExtension = Arrays.asList("jpg", "png", "jpeg");
			if (!allowedExtension.contains(extension)) {
				throw new IllegalArgumentException("Only png,jpg,jpeg is Supported");
			}
			String displayFileName = getDisplayFileName(originalFilename, extension);

			String randomName = UUID.randomUUID().toString();
			String uploadFileName = randomName.concat(extension);

			long fileSize = file.getSize();
			File newFile = new File(folderName);

			if (!newFile.exists()) {
				newFile.mkdir();
			}
			String path = folderName.concat(uploadFileName);

			long copy = Files.copy(file.getInputStream(), Path.of(path));

			if (copy != 0) {
				FileDetails fileDetails = FileDetails.builder().displayFileName(displayFileName).fileSize(fileSize)
						.originalFileName(originalFilename).path(path).uploadFileName(uploadFileName).build();
				return fileRepo.save(fileDetails);

			}
		}
		return null;
	}

	private String getDisplayFileName(String originalFilename, String extension) {

		String displayName = "";
		String baseName = FilenameUtils.getBaseName(originalFilename);
		if (baseName.length() > 7) {
			displayName = baseName.substring(0, 8) + "." + extension;
		}
		return displayName;
	}

}
