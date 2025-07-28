package com.org.Blog_App_Api.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Blog_App_Api.model.Users;
import com.org.Blog_App_Api.repo.UserRepo;

@Service
public class UserDetlImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByEmail(username);
		if (ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return new AuthUser(user);
	}

}
