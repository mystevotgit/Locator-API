package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceDto {
    private String from;
    private String to;
    private double distanceInKm;
}
