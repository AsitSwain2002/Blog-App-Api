package com.org.Blog_App_Api.service.impl;

import java.util.UUID;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.ExceptionHandler.ResourceNotFoundException;
import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.Util.MailService;
import com.org.Blog_App_Api.dto.MailData;
import com.org.Blog_App_Api.dto.UpdatePasswordDataDto;
import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.UserRepo;
import com.org.Blog_App_Api.service.HomeService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MailService mailService;

	@Autowired
	private BCryptPasswordEncoder encoder;

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

	@Override
	public boolean forgetPassword(String userName, HttpServletRequest req) throws Exception {
		Users user = userRepo.findByEmail(userName);
		String baseUrl = AppUtil.getUrl(req);
		String subDomain = "/api/v1/home/verify-link?uId=";

		String random = UUID.randomUUID().toString();
		user.getUserVerification().setVerificationCode(random);
		userRepo.save(user);
		if (!ObjectUtils.isEmpty(user)) {
			String message = "<b>Hi [[username]], </b> <br>"
					+ "You requested for password update Click the below link to update passwors<br>"
					+ "<a href = [[url]]>Click Here</a> <br><br>" + "If you Don't do that please ignore it<br>"
					+ "<b>Your Thankfully,</b><br>" + "<b>NoteApp.com</b>";

			message = message.replace("[[username]]", user.getFirstName());
			message = message.replace("[[url]]",
					baseUrl + subDomain + user.getId() + "&vId=" + user.getUserVerification().getVerificationCode());

			MailData build = MailData.builder().subject("Password Update").title("Update Password").message(message)
					.to(user.getEmail()).build();
			mailService.sendMail(build);
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePassword(UpdatePasswordDataDto data) {

		Users user = userRepo.findById(data.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User Id not found"));

		// logic for update password only one time
		if (!user.getUserVerification().isPasswordUpdate()) {
			throw new IllegalArgumentException("Access Denied");
		}

		if (user.getUserVerification().getVerificationCode() != null) {
			throw new IllegalArgumentException("You are not Verified");
		}
		if (data.getPasword().equals(data.getReEnterPassword())) {

			if (data.getPasword().length() < 6 && !StringUtils.isEmpty(data.getPasword())) {
				throw new IllegalArgumentException("Password must be greater or eqal to 6");
			}
			user.getUserVerification().setVerificationCode(null);
			user.setPassword(encoder.encode(data.getPasword()));
			user.getUserVerification().setPasswordUpdate(false);
			userRepo.save(user);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean verifyLink(int uId, String vId) {
		Users user = userRepo.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User Id not found"));
		if (user.getUserVerification().getVerificationCode() == null) {
			throw new IllegalArgumentException("Already Verified");
		}
		if (vId.equals(user.getUserVerification().getVerificationCode())) {
			user.getUserVerification().setVerificationCode(null);
			user.getUserVerification().setPasswordUpdate(true);
			userRepo.save(user);
			return true;
		}
		return false;
	}

}
