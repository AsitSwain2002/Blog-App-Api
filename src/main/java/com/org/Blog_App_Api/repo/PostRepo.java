package com.org.Blog_App_Api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Blog_App_Api.model.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findAllByIsDeletedFalse();
}
