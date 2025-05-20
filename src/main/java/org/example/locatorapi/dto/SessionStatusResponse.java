package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionStatusResponse {
    private List<ParticipantStatusDto> participants;
    private List<DistanceDto> distances;
}
