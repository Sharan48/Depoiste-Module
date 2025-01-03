package com.scm2.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.EmailDto;
import com.scm2.service.EmailService;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmailWithHtml(@RequestBody EmailDto emailDto) {
        emailService.sendMailWithHtml(emailDto.getTo(), emailDto.getSubject(), emailDto.getMessage());

        return new ResponseEntity<>("Email Send successfully", HttpStatus.ACCEPTED);

    }

    @PostMapping(value = "/send-file", consumes = { "multipart/form-data" })
    public ResponseEntity<String> sendEmailWithFile(@ModelAttribute EmailDto emailDto,
            @RequestParam(value = "uploadFile") MultipartFile file) {

        try {
            emailService.sendMailWithAttachment(emailDto.getTo(), emailDto.getSubject(), emailDto.getMessage(),
                    file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Email Send successfully", HttpStatus.ACCEPTED);

    }

    @PostMapping(value = "/send-with-file", consumes = { "multipart/form-data" })
    public ResponseEntity<String> sendEmailWithMultipart(@ModelAttribute EmailDto emailDto,
            @RequestParam(value = "uploadFile") MultipartFile file) {

        emailService.sendMailWithAttachment(emailDto.getTo(), emailDto.getSubject(), emailDto.getMessage(), file);

        return new ResponseEntity<>("Email Send successfully", HttpStatus.ACCEPTED);

    }

}
