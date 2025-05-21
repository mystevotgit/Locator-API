package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {
    private String userId;
    private double lat;
    private double lon;
}

