package com.ga.thirdgear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("ThirdGear - Verify Your Email");
        message.setText("Welcome to ThirdGear!\n\n"
                + "Please verify your email by clicking the link below:\n\n"
                + "http://localhost:8080/auth/verify-email?token=" + token
                + "\n\nIf you did not create an account, please ignore this email.");
        mailSender.send(message);
    }


    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("ThirdGear - Password Reset Request");
        message.setText("Hi,\n\n"
                + "You requested to reset your password. Click the link below:\n\n"
                + "http://localhost:8080/auth/reset-password?token=" + token
                + "\n\nIf you did not request a password reset, please ignore this email.\n\n"
                + "This link will expire in 24 hours.");
        mailSender.send(message);
    }
}