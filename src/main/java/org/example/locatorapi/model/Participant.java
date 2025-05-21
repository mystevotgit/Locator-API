package org.example.locatorapi.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participant {
    @Id
    private String userId;
    private String displayName;
    @Embedded
    private GeoLocation location;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
