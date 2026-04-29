package com.techlab.service.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String baseUrl;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {

        String resetLink = baseUrl + "/reset?token=" + token;

        String html = "<h2>Recuperar contraseña</h2>" +
                "<p>Hacé click en el siguiente botón:</p>" +
                "<a href='" + resetLink + "' " +
                "style='padding:10px 20px;background:#007bff;color:white;text-decoration:none;'>"
                + "Cambiar contraseña</a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Recuperación de contraseña");
        helper.setText(html, true);

        mailSender.send(message);
    }
}
