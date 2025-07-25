package com.org.Blog_App_Api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Blog_App_Api.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

}
