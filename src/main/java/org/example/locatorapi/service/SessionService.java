package org.example.locatorapi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.example.locatorapi.dto.LocationUpdateRequest;
import org.example.locatorapi.dto.SessionCreateRequest;
import org.example.locatorapi.dto.SessionCreateResponse;
import org.example.locatorapi.dto.SessionJoinRequest;
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
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final Sender sender;

    public SessionService(SessionRepository sessionRepository, Sender sender) {
        this.sessionRepository = sessionRepository;
        this.sender            = sender;
    }

    public SessionCreateResponse createSession(final SessionCreateRequest request) {
        String sessionId = UUID.randomUUID().toString();
        Participant host = new Participant(request.getUserId(), request.getDisplayName(), null, null);
        int passCode = request.getPassCode() != null ? request.getPassCode() : generatePasscode();
        Session session  = new Session(sessionId, passCode, host.getUserId(), new ArrayList<>(List.of(host)), LocalDateTime.now());
        sessionRepository.save(session);
        String shareLink = "http://localhost:8080/api/v1/session/" + sessionId + "/join";
        List<String> inviteEmails = request.getInviteEmails();
        if (inviteEmails != null && !inviteEmails.isEmpty()) {
            sender.publish(new EmailShareLinkEvent(sessionId, shareLink, passCode, inviteEmails));
        }
        return new SessionCreateResponse(sessionId, shareLink, passCode, request.getUserId());
    }

    private int generatePasscode() {
        Random random = new Random();
        return 100_000 + random.nextInt(900_000);
    }

    public Session joinSession(String sessionId, SessionJoinRequest req) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(EntityNotFoundException::new);
        boolean exists = session.getParticipants().stream().anyMatch(p -> p.getUserId().equals(req.getUserId()));
        if (!exists) {
            Participant newParticipant = new Participant(req.getUserId(), req.getDisplayName(), null, null);
            if (session.getPassCode() == req.getPassCode()) {
                session.getParticipants().add(newParticipant);
                session = sessionRepository.save(session);
            }
        }
        return session;
    }

    public void updateLocation(String sessionId, LocationUpdateRequest req) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(EntityNotFoundException::new);
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

    public void deleteSession(String sessionId, String userId) throws BadRequestException {
        Session session = sessionRepository.findById(sessionId).orElseThrow(EntityNotFoundException::new);
        if (session.getCreatorUserId().equals(userId)) {
            sessionRepository.deleteById(sessionId);
        } else {
            throw new BadRequestException("Only host can delete a session");
        }
    }

    public void leaveSession(String sessionId, String userId) throws BadRequestException {
        Session session = sessionRepository.findById(sessionId).orElseThrow(EntityNotFoundException::new);
        if (session.getCreatorUserId().equals(userId)) {
            deleteSession(sessionId, userId);
            return;
        }
        session.getParticipants().removeIf(p -> p.getUserId().equals(userId));
        sessionRepository.save(session);
    }

}
