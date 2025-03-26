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
	private int Created_by;
	@CreatedDate
	@Column(updatable = false)
	private Date created_on;
	@LastModifiedBy
	@Column(insertable = false)
	private int update_by;
	@LastModifiedDate
	@Column(insertable = false)
	private Date update_on;

}
