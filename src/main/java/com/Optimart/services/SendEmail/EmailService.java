package com.Optimart.services.SendEmail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${support.email}")
    private String supportEmail;

    @Value("${spring.mail.username}")
    private String emailHost;

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String resetToken) {
        String subject = "Reset Your Password for " + appName;
        String resetPasswordLink = "http://localhost:3000/reset-password?secretKey=" + resetToken;

        String body = "Dear User,\n\n"
                + "We received a request to reset the password for your " + appName + " account. "
                + "If you did not make this request, please ignore this email. "
                + "Otherwise, you can reset your password using the following link:\n\n"
                + resetPasswordLink + "\n\n"
                + "This link will expire in 15 minutes. If the link does not work, copy and paste it into your browser.\n\n"
                + "For your security, do not share this email or the password reset link with anyone.\n\n"
                + "If you need any assistance, please contact our support team at " + supportEmail + ".\n\n"
                + "Thank you,\n"
                + "The " + appName + " Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailHost);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
