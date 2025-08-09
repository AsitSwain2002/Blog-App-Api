package com.org.Blog_App_Api.Config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.org.Blog_App_Api.Util.AppUtil;
import com.org.Blog_App_Api.model.Users;

public class AuditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		Users user = AppUtil.getLoggedUser();
		return Optional.of(user.getId());
	}

}
