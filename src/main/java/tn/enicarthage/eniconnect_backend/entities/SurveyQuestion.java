package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey_questions")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_template_id", nullable = false)
    private SurveyTemplate surveyTemplate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String questionType; // "LIKERT" or "FREE_TEXT"

    private Integer questionOrder;

    @Column(columnDefinition = "TEXT") // Change from JSON to TEXT
    private String options;
}