package com.scm2.service;

public interface SmsService {
    void sendSms(String toMobileNumber, String message);

    void sendOtp(String toMobileNumber, String message);
}
