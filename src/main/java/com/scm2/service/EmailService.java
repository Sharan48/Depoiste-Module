package com.scm2.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    void sendMail(String to, String subject, String body);

    void sendMailWithHtml(String to, String subject, String htmlContent);

    void sendMailWithAttachment(String to, String subject, String message, File file);

    void sendEmailWithAttachment(String to, String subject, String message, String file);

    void sendMailWithAttachment(String to, String subject, String message, InputStream is);

    void sendMailWithAttachment(String to, String subject, String message, MultipartFile file);

}
