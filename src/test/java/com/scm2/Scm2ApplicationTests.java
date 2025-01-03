package com.scm2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm2.repositaries.UserRepository;
import com.scm2.service.EmailService;
import com.scm2.servicesimp.SmsServiceImp;
import com.scm2.servicesimp.DepositeModule.DepositeServiceImp;

@SpringBootTest(classes = Scm2Application.class)
class Scm2ApplicationTests {

	@Test
	void contextLoads() {

	}

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DepositeServiceImp depositeServiceImp;

	@Autowired
	private SmsServiceImp smsServiceImp;

	@Test
	void sendMailTest() {
		emailService.sendMail("sharanu318@gmail.com", "Just testing mail service",
				"This is scm project working on emailservices");
	}

	@Test
	void sendMailWithHtml() {

		String htmlContent = "" + "<h1 style='color:red;border:1px solid red;'>Welcome come to my project</h1>" + "";

		emailService.sendMailWithHtml("sharanu318@gmail.com", "Just testing mail service", htmlContent);

	}

	@Test
	void sendMailWithAttachment() {
		emailService.sendMailWithAttachment("sharanu318@gmail.com", "Sending attachment",
				"Sending mail with attachment", new File("src/main/resources/images/image (23).jpeg"));
	}

	@Test
	void sendMailWithAttachmentFile() throws Throwable {

		File file = new File("src/main/resources/images/image (23).jpeg");
		InputStream is = new FileInputStream(file);
		emailService.sendMailWithAttachment("sharanu318@gmail.com", "Sending image file",
				"Sending mail with image file attachment", is);
	}

	@Test
	void findUserList() {
		System.out.println("Testing user list");
		userRepository.findAll().forEach(list -> System.out.println(list.getUsername()));
	}

	@Test
	void sendSmsToUser() {
		smsServiceImp.sendSms("767676714745", "Hello from scm project");
	}

}
