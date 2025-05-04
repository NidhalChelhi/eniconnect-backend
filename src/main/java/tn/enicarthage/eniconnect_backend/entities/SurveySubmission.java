package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "survey_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveySubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @Column(length = 2000, columnDefinition = "TEXT")
    private String openFeedback; // Free-form comment at the end

    @Column(name = "submitted_at")
    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();

    // Ensures a student can't submit twice for the same survey
    @Column(name = "is_submitted", nullable = false)
    @Builder.Default
    private boolean isSubmitted = false;
}
