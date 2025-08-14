package com.org.Blog_App_Api.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.org.Blog_App_Api.dto.MailData;

import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	public void sendMail(MailData data) throws Exception {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(data.getTo());
		helper.setSubject(data.getSubject());
		helper.setText(data.getMessage(), true);
		helper.setFrom(sender, data.getTitle());
		javaMailSender.send(message);
	}
}
