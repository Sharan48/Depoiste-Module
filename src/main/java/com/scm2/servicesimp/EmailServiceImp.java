package com.scm2.servicesimp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImp implements EmailService {

    @Value("${spring.mail.properties..mail.domain_name}")
    private String domainName;

    @Autowired
    private JavaMailSender mailSender;

    org.slf4j.Logger log = LoggerFactory.getLogger(EmailServiceImp.class);

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);
        mailSender.send(message);

    }

    @Override
    public void sendMailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage simpleMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(simpleMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(htmlContent, true);
            mailSender.send(simpleMessage);
            log.info("Email sent successfully to: ", to);

        } catch (MessagingException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String message, File file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(message);
            FileSystemResource resorce = new FileSystemResource(file);
            helper.addAttachment(resorce.getFilename(), file);
            mailSender.send(mimeMessage);
            log.info("Email sent successfully with attachment to: ", to);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(message);
            File file = new File("src/main/resources/images/test.jpg");
            try {
                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                FileSystemResource filesystem = new FileSystemResource(file);
                helper.addAttachment(filesystem.getFilename(), file);
                mailSender.send(mimeMessage);
            } catch (IOException e) {

            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String message, MultipartFile file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(message);
            helper.addAttachment(file.getOriginalFilename(), file);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String message, String filePath) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(message);
            FileSystemResource resorce = new FileSystemResource(new File(filePath));
            helper.addAttachment(resorce.getFilename(), resorce);
            mailSender.send(mimeMessage);
            log.info("Email sent successfully with attachment to: ", to);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
