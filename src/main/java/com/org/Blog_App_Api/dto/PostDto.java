package com.org.Blog_App_Api.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private Integer id;
	private String title;
	private String content;
	private Date created_on;
	private int Created_by;
	private int update_by;
	private Date update_on;
	private boolean deleted;
	private FileDetailsDto fileDetails;
	private CategoryDto category;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class CategoryDto {
		private Integer id;
		private String name;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class FileDetailsDto {
		private Integer id;
		private String path;

		public String getImage_url() {
			if (path != null) {
				return "http://localhost:8080/" + path;
			}
			return null;
		}
	}
}
