package com.org.Blog_App_Api.validation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.UsersValidationException;
import com.org.Blog_App_Api.Util.AppConstant;
import com.org.Blog_App_Api.dto.UsersDto;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.RoleRepo;
import com.org.Blog_App_Api.repo.UserRepo;

@Component
public class UserValidation {

	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo userRepo;

	public void validateUser(UsersDto userDto) {

		Map<String, Object> errors = new LinkedHashMap<String, Object>();

		if (ObjectUtils.isEmpty(userDto)) {
			throw new IllegalArgumentException("Invalid Json/Text");
		} else {
			if (userDto.getFirstName().length() < 3) {
				errors.put("firstName", "First Name too short");
			}
			if (userDto.getFirstName().length() > 20) {
				errors.put("firstName", "First Name too big");
			}
			if (userDto.getLastName().length() < 3) {
				errors.put("laststName", "First Name too short");
			}
			if (userDto.getLastName().length() > 20) {
				errors.put("lastName", "First Name too big");
			}
			if (!userDto.getMobile().matches(AppConstant.MOBILE_REGEX)) {
				errors.put("mobile", "Invalid Contact No");
			}
			if (userDto.getPassword().length() < 6) {
				errors.put("password", "Password Must be 6 Letter");
			}
			if (!userDto.getEmail().matches(AppConstant.EMAIL_REGEX)) {
				errors.put("email", "Invalid Email");
			}

			// Email Exist or not Check
			boolean isEmailExist = emailCheck(userDto.getEmail());
			if (isEmailExist) {
				throw new IllegalArgumentException("Email Already Exist");
			}
			List<Integer> roleId = roleRepo.findAll().stream().map(id -> id.getId()).toList();
			List<Integer> allIds = userDto.getRole().stream().map(role -> role.getId())
					.filter(id -> !roleId.contains(id)).toList();

			if (!CollectionUtils.isEmpty(allIds)) {
				errors.put("role", "Invalid Role");
			}

			if (!ObjectUtils.isEmpty(errors)) {
				throw new UsersValidationException(errors);
			}
		}
	}

	private boolean emailCheck(String email) {
		Users findByEmail = userRepo.findByEmail(email);
		if (ObjectUtils.isEmpty(findByEmail)) {
			return false;
		}
		return true;
	}
}
