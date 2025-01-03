package com.scm2.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Helper {

    public static String getLinkForEmailVerification(String verificationToken) {
        // generate link for email verification
        return "http://localhost:8081/verify-email?token=" + verificationToken;
    }

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("7676767676");
        System.out.println(hashedPassword);
        if (encoder.matches("7676767676", hashedPassword)) {
            System.out.println("Password is correct");
        } else {
            System.out.println("Password is incorrect");
        }

    }

}
