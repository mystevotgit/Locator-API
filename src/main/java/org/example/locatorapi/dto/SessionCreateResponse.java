package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionCreateResponse {
    private String sessionId;
    private String shareLink;
    private int passCode;
    private String userId;
}
