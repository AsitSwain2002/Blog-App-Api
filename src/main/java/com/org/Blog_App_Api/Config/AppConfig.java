package com.org.Blog_App_Api.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	@Bean
	public AuditorAware<Integer> auditAware() {
		return new AuditAwareConfig();
	}
}
