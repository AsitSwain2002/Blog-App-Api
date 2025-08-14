package com.org.Blog_App_Api.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.org.Blog_App_Api.model.Post;
import com.org.Blog_App_Api.repo.PostRepo;

@Component
public class PostScheduler {

	@Autowired
	private PostRepo postRepo;

	@Scheduled(cron = "0 0 0 * * ?")
	public void scheduleDeletePost() {

		LocalDateTime autoDeleteDays = LocalDateTime.now().minusDays(7);
		List<Post> deleteNote = postRepo.findAllByDeletedAndDeletedOnBefore(true, autoDeleteDays);
		postRepo.deleteAll(deleteNote);
	}
}
