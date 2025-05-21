package org.example.locatorapi.service;

import lombok.RequiredArgsConstructor;
import org.example.locatorapi.dto.LocationUpdateRequest;
import org.example.locatorapi.dto.SessionCreateResponse;
import org.example.locatorapi.events.EmailShareLinkEvent;
import org.example.locatorapi.kafka.Sender;
import org.example.locatorapi.model.GeoLocation;
import org.example.locatorapi.model.Participant;
import org.example.locatorapi.model.Session;
import org.example.locatorapi.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final Sender sender;

    public SessionService(SessionRepository sessionRepository, Sender sender) {
        this.sessionRepository = sessionRepository;
        this.sender            = sender;
    }

    public SessionCreateResponse createSession(String displayName) {
        String sessionId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Participant participant = new Participant();
        participant.setUserId(userId);
        participant.setDisplayName(displayName);
        Session session = new Session(sessionId, new ArrayList<>(List.of(participant)), LocalDateTime.now());
        sessionRepository.save(session);
        return new SessionCreateResponse(sessionId, null, userId);
    }

    public SessionCreateResponse createSession(String displayName, List<String> inviteEmails) {

        String sessionId = UUID.randomUUID().toString();
        String userId    = UUID.randomUUID().toString();

        Participant host = new Participant(userId, displayName, null, null);
        Session session  = new Session(sessionId, new ArrayList<>(List.of(host)), LocalDateTime.now());

        sessionRepository.save(session);

        String shareLink = "http://localhost:8080/api/v1/session/join/" + sessionId;

        if (inviteEmails != null && !inviteEmails.isEmpty()) {
            sender.publish(new EmailShareLinkEvent(sessionId, shareLink, inviteEmails));
        }

        return new SessionCreateResponse(sessionId, shareLink, userId);
    }

    public Session joinSession(String sessionId, String userId, String displayName) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        boolean exists = session.getParticipants().stream().anyMatch(p -> p.getUserId().equals(userId));
        if (!exists) {
            Participant newParticipant = new Participant(userId, displayName, null, null);
            session.getParticipants().add(newParticipant);
            session = sessionRepository.save(session);
        }
        return session;
    }

    public void updateLocation(String sessionId, LocationUpdateRequest req) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        for (Participant p : session.getParticipants()) {
            if (p.getUserId().equals(req.getUserId())) {
                p.setLocation(new GeoLocation(req.getLat(), req.getLon()));
                p.setLastUpdated(LocalDateTime.now());
                break;
            }
        }
        sessionRepository.save(session);
    }

    public Session getSession(String sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow();
    }
}
