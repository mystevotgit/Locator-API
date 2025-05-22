package org.example.locatorapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {
    private String recipient;
    private String subject;
    private String body;
}
