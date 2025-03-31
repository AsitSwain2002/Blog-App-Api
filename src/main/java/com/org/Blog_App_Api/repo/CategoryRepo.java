package com.org.Blog_App_Api.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Blog_App_Api.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

	boolean existsByNameAndCatDeletedFalse(String name);

	List<Category> findByCatDeletedFalse();

	Optional<Category> findByIdAndCatDeletedFalse(Integer id);
}
