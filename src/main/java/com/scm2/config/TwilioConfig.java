package com.scm2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@Configuration
public class TwilioConfig {

    @Value("${sms.Account_SID}")
    private String accountSid;

    @Value("${sms.Auth_Token}")
    private String authToken;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

}
