package org.example.locatorapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class SessionCreateRequest {
    private String displayName;
    private List<String> inviteEmails;
}