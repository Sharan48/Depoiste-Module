package com.scm2.servicesimp;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scm2.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsServiceImp implements SmsService {

    @Value("${sms.My_Twilio_phonenumber}")
    private String twilioPhoneNumber;

    @Override
    public void sendSms(String toMobileNumber, String message) {
        Message.creator(new PhoneNumber(toMobileNumber), new PhoneNumber(twilioPhoneNumber), message).create();
    }

    @Override
    public void sendOtp(String toMobileNumber, String message) {
        SecureRandom secureRandom = new SecureRandom();
        String otps = String.format("%06d", secureRandom.nextInt(100000));
        String messageBody = message + " " + otps;
        Message.creator(new PhoneNumber(toMobileNumber), new PhoneNumber(twilioPhoneNumber), messageBody)
                .create();
    }

}
