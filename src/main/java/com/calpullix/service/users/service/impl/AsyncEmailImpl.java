package com.calpullix.service.users.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.calpullix.service.users.service.AsyncEmail;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncEmailImpl implements AsyncEmail {

	private static final String LOGO_MAIl = "logo_CalpulliX.png";

	private static final String EMAIL_TEMPLATE = "emailTemplate.html";

	private static final String NAME_KEY_TEMPLATE = "${name}";

	private static final String PASWORD_KEY_TEMPLATE = "${password}";

	@Autowired
	private JavaMailSender sender;
	
	@Value("${app.subject-email}")
	private String subjectEmail;

	@Value("${app.id-image}")
	private String idImage;

	
	@Override
	@Async
	@Timed(value = "calpullix.service.email.users.metrics", description = "Restart password operation")
	public void sendEmail(
			String password, 
			String name, 
			String email) throws MessagingException {
		
		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message, Boolean.TRUE);

		final ClassPathResource htmlFile = new ClassPathResource(EMAIL_TEMPLATE);
		String htmlContent = "";
		try {
			final Map<String, String> values = new HashMap<>();
			values.put(NAME_KEY_TEMPLATE, name);
			values.put(PASWORD_KEY_TEMPLATE, password);
			htmlContent = readFile(htmlFile.getFile(), values);
			log.info(":: HTML {} ", htmlContent);
		} catch (Exception e) {
			log.error(":: Error File Mail ", e);
			e.printStackTrace();
		}

		helper.setTo(email);
		helper.setText(htmlContent, true);
		helper.setSubject(subjectEmail);

		final ClassPathResource file = new ClassPathResource(LOGO_MAIl);
		helper.addInline(idImage, file);

		sender.send(message);
		
		log.info(":: The register email has been sended ");
	}
	
	private String readFile(File file, Map<String, String> values) throws IOException {
		String result = new String();
		final Path path = Paths.get(file.getAbsolutePath());
		final List<String> lines = Files.readAllLines(path);
		for (final String line : lines) {
			result+=line;
		}
		
		for (final String key : values.keySet()) {
			if (result.contains(key)) {
				result = result.replace(key, values.get(key));
			} 
		}
		return result;
	}


}
