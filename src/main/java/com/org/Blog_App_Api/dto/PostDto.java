package com.org.Blog_App_Api.dto;

import java.util.Date;

import com.org.Blog_App_Api.model.Category;
import com.org.Blog_App_Api.model.FileDetails;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private String title;
	private String content;
	private Date createdOn;
	private int CreatedBy;
	private Date updateOn;
	private boolean isDleted;
	private FileDetails fileDetails;
	private Category category;
} 
