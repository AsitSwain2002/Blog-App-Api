package com.org.Blog_App_Api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Blog_App_Api.model.FevoritePost;

@Repository
public interface FevoriteRepo extends JpaRepository<FevoritePost, Integer> {

	List<FevoritePost> findAllByUsersId(int userId);

	FevoritePost findByPostId(int postId);

}
