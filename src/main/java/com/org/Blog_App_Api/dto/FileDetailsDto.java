package com.org.Blog_App_Api.dto;

import lombok.Data;

@Data
public class FileDetailsDto {
	private Integer id;
	private String displayFileName;
	private String uploadFileName;
	private String originalFileName;
	private Long fileSize;
	private String path;
}
