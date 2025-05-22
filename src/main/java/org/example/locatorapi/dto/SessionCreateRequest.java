package org.example.locatorapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class SessionCreateRequest {
    private String displayName;
    private Integer passCode;
    private String userId;
    private List<String> inviteEmails;
}