package com.techlab.service.implementation;

import com.techlab.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("emailService")
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String baseUrl;

    @Override
    public void sendPasswordResetEmail(String to, String userName, String token) {

        String resetLink = baseUrl + "/reset?token=" + token;

        // Versión texto plano (fallback para clientes que no soportan HTML)
        String text = "Hola " + userName + ",\n\n"
                + "Recibimos una solicitud para restablecer tu contraseña en TechLab.\n\n"
                + "Hacé click en el siguiente link:\n"
                + resetLink + "\n\n"
                + "Este link expira en 10 minutos.\n\n"
                + "Si no solicitaste este cambio, ignorá este mensaje.";

        // Versión HTML
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='color-scheme' content='light dark'>" +
                "<meta name='supported-color-schemes' content='light dark'>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;'>" +
                "<table role='presentation' style='width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden;'>" +
                "<tr><td style='padding: 40px 30px;'>" +
                "<h2 style='color: #333; margin-top: 0;'>Recuperación de contraseña</h2>" +
                "<p style='color: #555; font-size: 16px; line-height: 1.5;'>Hola <strong>" + userName + "</strong>,</p>" +
                "<p style='color: #555; font-size: 16px; line-height: 1.5;'>Recibimos una solicitud para restablecer tu contraseña en <strong>TechLab</strong>.</p>" +
                "<p style='color: #555; font-size: 16px; line-height: 1.5;'>Hacé click en el siguiente botón:</p>" +
                "<table role='presentation' style='margin: 30px 0;'>" +
                "<tr><td style='background-color: #007bff; border-radius: 4px;'>" +
                "<a href='" + resetLink + "' " +
                "style='display: inline-block; padding: 12px 30px; color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold;'>" +
                "Restablecer contraseña</a>" +
                "</td></tr></table>" +
                "<p style='color: #999; font-size: 12px; line-height: 1.5;'>" +
                "Este link expira en <strong>10 minutos</strong>. Si no solicitaste este cambio, ignorá este mensaje.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 30px 0;'>" +
                "<p style='color: #aaa; font-size: 11px; text-align: center;'>" +
                "TechLab - Buenos Aires Aprende</p>" +
                "</td></tr></table></body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña - TechLab");
            helper.setText(text, html);  // text/plain + text/html en un solo llamado
        } catch (MessagingException e) {
            log.error("Error al preparar el email de recuperación para {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al preparar el email de recuperación", e);
        }

        mailSender.send(message);
        log.info("Email de recuperación enviado a {}", to);
    }
}
