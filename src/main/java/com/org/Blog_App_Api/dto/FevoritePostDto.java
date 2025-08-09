package com.org.Blog_App_Api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FevoritePostDto {
	private int id;
	private Integer usersId;
	private PostDto post;
}
