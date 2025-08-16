package com.org.Blog_App_Api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.dto.UpdatePasswordDataDto;
import com.org.Blog_App_Api.service.HomeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/verify")
	public ResponseEntity<?> verify(@RequestParam int uId, @RequestParam String vCode) {

		boolean verify = homeService.verify(uId, vCode);
		if (verify) {
			return ResponseBuilder.withMessageNoData("Verification Successful", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Invalid Link", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(@RequestParam String userName, HttpServletRequest req) throws Exception {

		boolean verify = homeService.forgetPassword(userName, req);
		if (verify) {
			return ResponseBuilder.withMessageNoData("Email Sent Successsfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Invalid Username", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/verify-link")
	public ResponseEntity<?> verifyLink(@RequestParam int uId, @RequestParam String vId) throws Exception {

		boolean verify = homeService.verifyLink(uId, vId);
		if (verify) {
			return ResponseBuilder.withMessageNoData("Verification Sucessful", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Code did not match", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDataDto data) throws Exception {

		boolean verify = homeService.updatePassword(data);
		if (verify) {
			return ResponseBuilder.withMessageNoData("Password Reset Successsfully", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Password Did not match", HttpStatus.BAD_REQUEST);
		}
	}

}
