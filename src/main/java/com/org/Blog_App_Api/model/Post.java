package com.org.Blog_App_Api.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Post extends BaseModel{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;
	private String title;
	private String content;
	private String image;
//	private List<Comments> comments;
	private int category;
	private boolean isDleted;
}
