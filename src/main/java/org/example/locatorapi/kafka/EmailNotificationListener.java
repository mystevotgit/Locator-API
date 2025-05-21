package org.example.locatorapi.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.locatorapi.events.EmailShareLinkEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationListener {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "${locator.kafka.topic.session-created}", groupId = "locator-notification")
    public void handle(EmailShareLinkEvent evt) {
        evt.getRecipients().forEach(to -> sendEmail(to, evt));
        log.info("Invitations sent for session {}", evt.getSessionId());
    }

    private void sendEmail(String to, EmailShareLinkEvent evt) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Join my Live Meet session " + evt.getSessionId());
        msg.setText("Click here to join: " + evt.getShareLink());
        mailSender.send(msg);
    }
}