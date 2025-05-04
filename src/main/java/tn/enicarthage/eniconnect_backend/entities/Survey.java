package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.*;
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

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Speciality speciality;

    @Column(nullable = false)
    private int semester; // 1 or 2

    @Column(nullable = false)
    private int level; // 1, 2, or 3

    @Column(name = "school_year", nullable = false, length = 9)
    private String schoolYear; // Format: "2023/2024"

    @Column(nullable = false)
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

    // Business logic methods
    public boolean isActive() {
        if (!isPublished) return false;

        LocalDateTime now = LocalDateTime.now();
        boolean isOpen = openDate == null || now.isAfter(openDate);
        boolean isNotClosed = closeDate == null || now.isBefore(closeDate);

        return isOpen && isNotClosed;
    }

    public void publish() {
        this.isPublished = true;
    }

    public void unpublish() {
        this.isPublished = false;
    }

    public boolean isCompletedByStudent(Student student) {
        return responses.stream()
                .anyMatch(r -> r.getStudent().equals(student) && r.isSubmitted());
    }
}