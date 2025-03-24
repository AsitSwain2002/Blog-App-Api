package com.org.Blog_App_Api.dto;

import java.util.Date;

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
	private String image;
	private Date createdOn;
	private int CreatedBy;
	private Date updateOn;
	private boolean isDleted;
}
