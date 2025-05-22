package org.example.locatorapi.controller;

import org.apache.coyote.BadRequestException;
import org.example.locatorapi.dto.*;
import org.example.locatorapi.model.Session;
import org.example.locatorapi.service.DistanceCalculator;
import org.example.locatorapi.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionCreateResponse> createSession(@RequestBody SessionCreateRequest req) {
        SessionCreateResponse response = sessionService.createSession(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/join")
    public ResponseEntity<Session> joinSession(@PathVariable String sessionId, @RequestBody SessionJoinRequest req) {
        return ResponseEntity.ok(sessionService.joinSession(sessionId, req));
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

    @DeleteMapping("/{sessionId}/user/{userId}")
    public ResponseEntity<String> deleteSession(@PathVariable String sessionId, @PathVariable String userId) throws BadRequestException {
        sessionService.deleteSession(sessionId, userId);
        return ResponseEntity.ok().body("Successfully deleted session");
    }

    @PatchMapping("/{sessionId}/user/{userId}/leave")
    public ResponseEntity<Void> leave(@PathVariable String sessionId, @PathVariable String userId) throws BadRequestException {
        sessionService.leaveSession(sessionId, userId);
        return ResponseEntity.ok().build();
    }
}
