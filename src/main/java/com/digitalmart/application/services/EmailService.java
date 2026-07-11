package com.digitalmart.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String email, String firstname){

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setSubject("Welcome to Daily Veggies!");
            mail.setText("Hey, "+firstname+"! Welcome to this platform where daily essentials delivered in minutes...");
            javaMailSender.send(mail);
            log.info("Email sent successfully.....");
        }catch (MailException e){
            log.error(e.getMessage());
        }
    }
}
