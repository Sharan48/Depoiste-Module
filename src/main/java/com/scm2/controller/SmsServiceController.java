package com.scm2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm2.service.SmsService;

@RestController
@RequestMapping("/sms")
public class SmsServiceController {

    private SmsService smsService;

    public SmsServiceController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/send")
    public ResponseEntity<String> addSmsService() {
        smsService.sendSms("+917676714748", "First Spring boot sms service");
        return new ResponseEntity<>("Successfully sms send", HttpStatus.ACCEPTED);

    }

    @GetMapping("/sendOtp")
    public ResponseEntity<String> sendOtp() {
        smsService.sendOtp("+917676714748", "Your OTP is:");
        return new ResponseEntity<>("Successfully sms send", HttpStatus.ACCEPTED);

    }

}
