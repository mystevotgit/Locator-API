package org.example.locatorapi.controller;

import org.example.locatorapi.dto.LocationUpdateRequest;
import org.example.locatorapi.dto.SessionCreateResponse;
import org.example.locatorapi.dto.SessionJoinRequest;
import org.example.locatorapi.dto.SessionStatusResponse;
import org.example.locatorapi.model.Session;
import org.example.locatorapi.service.DistanceCalculator;
import org.example.locatorapi.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/{displayName}")
    public ResponseEntity<SessionCreateResponse> createSession(@PathVariable String displayName) throws MalformedURLException {
        SessionCreateResponse response = sessionService.createSession(displayName);
        response.setShareLink(new URL("http://localhost:8080/api/v1/session/join/" + response.getSessionId()).toExternalForm());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/join")
    public ResponseEntity<Session> joinSession(@PathVariable String sessionId, @RequestBody SessionJoinRequest req) {
        return ResponseEntity.ok(sessionService.joinSession(sessionId, req.getUserId(), req.getDisplayName()));
    }

    @PostMapping("/{sessionId}/location")
    public ResponseEntity<Void> updateLocation(@PathVariable String sessionId, @RequestBody LocationUpdateRequest req) {
        sessionService.updateLocation(sessionId, req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/status")
    public ResponseEntity<SessionStatusResponse> getStatus(@PathVariable String sessionId) {
        Session session = sessionService.getSession(sessionId);
        return ResponseEntity.ok(DistanceCalculator.calculate(session));
    }
}
