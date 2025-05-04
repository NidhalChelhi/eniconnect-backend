package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500, columnDefinition = "TEXT")
    private String text; // e.g., "Les objectifs visés sont clairement annoncés"

    @Column(nullable = false)
    @Builder.Default
    private int minValue = 1; // Likert scale minimum (1 = "Not satisfied")

    @Column(nullable = false)
    @Builder.Default
    private int maxValue = 4; // Likert scale maximum (4 = "Satisfied")

    @Column(nullable = false)
    private int orderIndex; // Display order (1 to 8)

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
