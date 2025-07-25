package com.org.Blog_App_Api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.ResourceNotFoundException;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.UserRepo;
import com.org.Blog_App_Api.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public boolean verify(int userId, String vCode) {

		if (userId != 0 && vCode != null) {
			Users user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

			String verificationCode = user.getUserVerification().getVerificationCode();
			if (!ObjectUtils.isEmpty(user) && vCode.equals(verificationCode)) {
				user.getUserVerification().setActive(true);
				user.getUserVerification().setVerificationCode(null);
				userRepo.save(user);
				return true;
			}
		}
		return false;
	}

}
