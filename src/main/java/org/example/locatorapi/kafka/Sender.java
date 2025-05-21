package org.example.locatorapi.kafka;

import lombok.RequiredArgsConstructor;
import org.example.locatorapi.events.EmailShareLinkEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Sender {

    private final KafkaTemplate<String, EmailShareLinkEvent> kafka;
    @Value("${locator.kafka.topic.session-created}")
    private String topic;

    public void publish(EmailShareLinkEvent evt) {
        kafka.send(topic, evt.getSessionId(), evt);
    }
}
