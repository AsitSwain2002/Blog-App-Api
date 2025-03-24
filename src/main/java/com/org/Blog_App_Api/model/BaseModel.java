package com.org.Blog_App_Api.model;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {

	private Date createdOn;
	private int CreatedBy;
	private Date updateOn;
	private int updateBy;
}
