package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionJoinRequest {
    private String userId;
    private String displayName;
    private int passCode;
}
