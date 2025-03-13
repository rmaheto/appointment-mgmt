package com.codemaniac.appointment.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String emailFrom;

  public void sendEmail(final String to, final String subject, final String body) {
    try {
      final MimeMessage message = mailSender.createMimeMessage();
      final MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(emailFrom);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);

      mailSender.send(message);
      log.info("Email sent successfully to {}", to);
    } catch (final MessagingException e) {
      log.error("Failed to send email to {}: {}", to, e.getMessage());
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
