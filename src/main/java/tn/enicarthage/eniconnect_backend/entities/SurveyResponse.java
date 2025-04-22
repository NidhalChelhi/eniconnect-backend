package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey_responses")
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private LocalDateTime submittedAt;

    @Column(nullable = false)
    private Boolean isComplete;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL)
    private List<CourseResponse> courseResponses;

    private String feedback; // For the final free-form feedback
}