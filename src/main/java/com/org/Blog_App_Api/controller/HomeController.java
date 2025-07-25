package com.org.Blog_App_Api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.Blog_App_Api.Util.ResponseBuilder;
import com.org.Blog_App_Api.service.HomeService;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/verify")
	public ResponseEntity<?> verify(@RequestParam int uId, @RequestParam String vCode) {

		boolean verify = homeService.verify(uId, vCode);
		if (verify) {
			return ResponseBuilder.withMessageNoData("Verification Successsful", HttpStatus.OK);
		} else {
			return ResponseBuilder.withMessageNoData("Invalid Link", HttpStatus.BAD_REQUEST);
		}
	}
}
