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
	private Date created_on;
	private int Created_by;
	private Date update_on;
	private int update_by;
}
