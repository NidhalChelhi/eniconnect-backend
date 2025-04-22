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
@Table(name = "surveys")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_template_id", nullable = false)
    private SurveyTemplate surveyTemplate;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @Column(nullable = false)
    private Integer yearOfStudy; // 1, 2, or 3

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime openDate;
    private LocalDateTime closeDate;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "survey")
    private List<SurveyResponse> responses;
}