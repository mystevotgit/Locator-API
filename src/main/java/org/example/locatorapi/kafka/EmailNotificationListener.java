package org.example.locatorapi.kafka;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.locatorapi.dto.EmailDto;
import org.example.locatorapi.events.EmailShareLinkEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailNotificationListener {

    public static final String JOIN_MY_LIVE_MEET_SESSION = "Join my Live Meet session ";
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @KafkaListener(topics = "${locator.kafka.topic.session-created}", groupId = "locator-notification")
    public void handle(EmailShareLinkEvent evt) {
        evt.getRecipients().forEach(to -> sendEmail(new EmailDto(to,
                JOIN_MY_LIVE_MEET_SESSION + evt.getSessionId(),
                "URL: " + evt.getShareLink() + " , passCode: " + evt.getPassCode())));
        log.info("Invitations sent for session {}", evt.getSessionId());
    }

    public Boolean sendEmail(EmailDto emailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(emailDto.getRecipient());
            helper.setText(emailDto.getBody(), true);
            helper.setSubject(emailDto.getSubject());
            mailSender.send(message);
            log.info("Email sent");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}