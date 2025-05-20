package org.example.locatorapi.service;

import org.example.locatorapi.dto.DistanceDto;
import org.example.locatorapi.dto.ParticipantStatusDto;
import org.example.locatorapi.dto.SessionStatusResponse;
import org.example.locatorapi.model.Participant;
import org.example.locatorapi.model.Session;
import org.example.locatorapi.util.HaversineUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistanceCalculator {

    public static SessionStatusResponse calculate(Session session) {
        List<ParticipantStatusDto> participantDtos = new ArrayList<>();
        List<DistanceDto> distanceDtos = new ArrayList<>();

        List<Participant> participants = session.getParticipants();

        for (Participant p : participants) {
            if (p.getLocation() != null) {
                participantDtos.add(new ParticipantStatusDto(
                        p.getUserId(),
                        p.getDisplayName(),
                        p.getLocation().getLatitude(),
                        p.getLocation().getLongitude()
                ));
            }
        }

        // Compute pairwise distances
        for (int i = 0; i < participants.size(); i++) {
            Participant p1 = participants.get(i);
            if (p1.getLocation() == null) continue;

            for (int j = i + 1; j < participants.size(); j++) {
                Participant p2 = participants.get(j);
                if (p2.getLocation() == null) continue;

                double distance = HaversineUtil.calculate(
                        p1.getLocation().getLatitude(),
                        p1.getLocation().getLongitude(),
                        p2.getLocation().getLatitude(),
                        p2.getLocation().getLongitude()
                );

                distanceDtos.add(new DistanceDto(
                        p1.getUserId(),
                        p2.getUserId(),
                        distance
                ));
            }
        }

        return new SessionStatusResponse(participantDtos, distanceDtos);
    }
}

