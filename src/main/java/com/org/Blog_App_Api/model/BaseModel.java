package com.org.Blog_App_Api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {

	@CreatedBy
	@Column(updatable = false)
	private int createdBy;
	@CreatedDate
	@Column(updatable = false)
	private Date createdOn;
	@LastModifiedBy
	@Column(insertable = false)
	private int updateBy;
	@LastModifiedDate
	@Column(insertable = false)
	private Date updateOn;

}
