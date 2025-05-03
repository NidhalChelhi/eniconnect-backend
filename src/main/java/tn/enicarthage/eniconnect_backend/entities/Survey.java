package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "surveys", indexes = {
        @Index(name = "idx_survey_speciality_level_semester", columnList = "speciality, level, semester"),
        @Index(name = "idx_survey_school_year", columnList = "school_year")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "speciality", nullable = false, length = 20)
    private Speciality speciality;

    @Column(name = "semester", nullable = false)
    private int semester; // 1 or 2

    @Column(name = "level", nullable = false)
    private int level; // 1, 2, or 3

    @Column(name = "school_year", nullable = false, length = 9)
    private String schoolYear; // Format: "2023/2024"

    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private boolean isPublished = false;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "survey_courses",
            joinColumns = @JoinColumn(name = "survey_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> targetCourses;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.REMOVE)
    private List<SurveyResponse> responses;


    // Helper method to check if survey is currently active
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return isPublished &&
                (openDate == null || now.isAfter(openDate)) &&
                (closeDate == null || now.isBefore(closeDate));
    }

    // Helper method to check if student has completed this survey
    public boolean isCompletedByStudent(Student student) {
        return responses.stream()
                .anyMatch(r -> r.getStudent().equals(student) && r.isSubmitted());
    }
}