package com.org.Blog_App_Api.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

	private String name;
	private boolean isDeleted;
	private Date createdOn;
	private int CreatedBy;
}
