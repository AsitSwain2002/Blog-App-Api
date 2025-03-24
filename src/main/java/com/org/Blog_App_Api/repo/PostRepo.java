package com.org.Blog_App_Api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.Blog_App_Api.model.Post;

public interface PostRepo extends JpaRepository<Post, Integer> {

}
