package com.org.Blog_App_Api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.Util.MailService;
import com.org.Blog_App_Api.dto.MailData;
import com.org.Blog_App_Api.dto.UsersDto;
import com.org.Blog_App_Api.model.FileDetails;
import com.org.Blog_App_Api.model.Role;
import com.org.Blog_App_Api.model.UserVerification;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.FileRepo;
import com.org.Blog_App_Api.repo.RoleRepo;
import com.org.Blog_App_Api.repo.UserRepo;
import com.org.Blog_App_Api.service.UserService;
import com.org.Blog_App_Api.validation.UserValidation;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserSerciceImpl implements UserService {

	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private ModelMapper mapper;
	@Value("${file.upload.path}")
	private String folderName;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserValidation userValidation;
	@Autowired
	private MailService mailService;

	@Override
	public boolean registerUser(String reqUser, MultipartFile file, String url) throws Exception {

		UsersDto usersDto = objMapper.readValue(reqUser, UsersDto.class);
		// user Validation
		userValidation.validateUser(usersDto);
		Users user = mapper.map(usersDto, Users.class);
		// save User
		FileDetails saveFile = saveFile(file);
		if (!ObjectUtils.isEmpty(saveFile)) {
			user.setFileDetails(saveFile);
		}
		// Set Role
		setRole(usersDto, user);
		setVerification(user);
		Users save = userRepo.save(user);
		if (!ObjectUtils.isEmpty(save)) {
			sendMail(user, url);
			return true;
		}
		return false;
	}

	private void setVerification(Users user) {
		UserVerification build = UserVerification.builder().verificationCode(UUID.randomUUID().toString()).build();
		user.setUserVerification(build);

	}

	private void sendMail(Users user, String url) throws Exception {

		String message = "<b>Hii [[userName]]</b><br>" + "Your Account Created Sucessfully <br>"
				+ "Click the below link for account verify<br>" + "<a href ='[[link]]'>Click Here </a><br><br>"
				+ "Thanks,<br>" + "owner";

		message = message.replace("[[userName]]", user.getFirstName());
		message = message.replace("[[link]]", url + "/api/v1/home/verify?uId=" + user.getId() + "&vCode="
				+ user.getUserVerification().getVerificationCode());

		String title = "Account Verification";

		MailData build = MailData.builder().title(title).to(user.getEmail()).message(message)
				.subject("Account Created Sucessfully").build();

		mailService.sendMail(build);

	}

	private void setRole(UsersDto usersDto, Users user) {

		List<Integer> collect = usersDto.getRole().stream().map(r -> r.getId()).collect(Collectors.toList());
		List<Role> role = roleRepo.findAllById(collect);
		user.setRole(role);
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
