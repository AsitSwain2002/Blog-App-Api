package com.org.Blog_App_Api.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.org.Blog_App_Api.repo.PostRepo;

@Component
public class PostScheduler {

	private PostRepo postRepo;
	@Scheduled(cron = "0 0 0 * * ?")
	public void scheduleDeletePost() {
		
		
	}
}
