package com.org.Blog_App_Api.dto;

import lombok.Data;

@Data
public class UpdatePasswordDataDto {
	private int userId;
	private String pasword;
	private String reEnterPassword;
}
