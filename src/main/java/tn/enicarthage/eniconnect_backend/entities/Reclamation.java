package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import tn.enicarthage.eniconnect_backend.enums.ReclamationStatus;
import tn.enicarthage.eniconnect_backend.enums.ReclamationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReclamationType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReclamationStatus status = ReclamationStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;
}
