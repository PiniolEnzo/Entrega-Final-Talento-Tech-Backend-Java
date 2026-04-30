package com.techlab.service.implementation;

import com.techlab.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("emailService")
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String baseUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {

        String resetLink = baseUrl + "/reset?token=" + token;

        String html = "<h2>Recuperar contraseña</h2>" +
                "<p>Hacé click en el siguiente botón:</p>" +
                "<a href='" + resetLink + "' " +
                "style='padding:10px 20px;background:#007bff;color:white;text-decoration:none;'>"
                + "Cambiar contraseña</a>";


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");
            helper.setText(html, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);

    }
}
