package org.example.locatorapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Session {
    @Id
    private String sessionId;

    private int passCode;

    private String creatorUserId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private List<Participant> participants = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
