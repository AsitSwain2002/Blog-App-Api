package com.org.Blog_App_Api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

	private int id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
	private FileDetailsDto fileDetailsDto;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class FileDetailsDto {
		private String displayFileName;
	}
}
