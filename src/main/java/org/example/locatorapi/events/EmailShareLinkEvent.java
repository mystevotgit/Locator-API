package org.example.locatorapi.events;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailShareLinkEvent {
    private String sessionId;
    private String shareLink;
    private int passCode;
    private List<String> recipients;
}
