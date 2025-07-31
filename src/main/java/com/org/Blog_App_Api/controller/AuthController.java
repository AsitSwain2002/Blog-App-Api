package com.org.Blog_App_Api.controller;

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

import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.dto.LoginRequest;
import com.org.Blog_App_Api.dto.LoginResponse;
import com.org.Blog_App_Api.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register-user")
	public ResponseEntity<?> saveUser(@RequestParam String userDto, @RequestParam(required = false) MultipartFile file,
			HttpServletRequest req) throws Exception {

		String url = AppUtil.getUrl(req);
		boolean registerUser = authService.registerUser(userDto, file, url);
		if (registerUser) {
			return ResponseBuilder.withMessageNoData("Register Successfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		LoginResponse user = authService.login(loginRequest);

		if (!ObjectUtils.isEmpty(user)) {
			return ResponseBuilder.withMessageAndData("Login Successfully", user, HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Invalid Credintial", HttpStatus.BAD_REQUEST);
		}
	}
}
