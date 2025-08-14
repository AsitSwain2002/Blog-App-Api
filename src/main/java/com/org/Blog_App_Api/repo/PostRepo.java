package com.org.Blog_App_Api.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.Blog_App_Api.model.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findAllByDeletedFalse();

	List<Post> findAllByCategoryId(int categoryId);

	@Query("SELECT p FROM Post p WHERE p.createdBy = :userId AND p.deleted = True")
	List<Post> findAllByCreatedByAndDeletedTure(@Param("userId") int userId);

	List<Post> findAllByDeletedAndDeletedOnBefore(boolean b, LocalDateTime autoDeleteDays);

	List<Post> findAllByCreatedBy(int userId);

}
