package com.ga.waslah.service;

import org.springframework.stereotype.Service;

@Service
public class MockEmailService {

    public void sendVerificationEmail(
            String email,
            String verificationLink
    ) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       WASLAH EMAIL VERIFICATION");
        System.out.println("========================================");
        System.out.println("To: " + email);
        System.out.println();
        System.out.println("Please verify your WASLAH account:");
        System.out.println();
        System.out.println(verificationLink);
        System.out.println();
        System.out.println("========================================");
        System.out.println();
    }
}

