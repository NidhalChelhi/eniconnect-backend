package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_speciality_level", columnList = "speciality, current_level"),
        @Index(name = "idx_student_email", columnList = "email", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricule", nullable = false, unique = true, length = 20)
    private String matricule;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "speciality", nullable = false, length = 20)
    private Speciality speciality;

    @Column(name = "current_level", nullable = false)
    private int currentLevel; // 1, 2, or 3

    @Column(name = "groupe", nullable = false, length = 1)
    private String groupe; // A/B/C/D

    @Column(name = "entry_school_year", nullable = false, length = 9)
    private String entrySchoolYear; // Format: "2023/2024"

    @Column(name = "graduation_school_year", length = 9)
    private String graduationSchoolYear; // Format: "2023/2024"

    @Column(name = "gender", nullable = false, length = 10)
    private String gender = "other";

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<SurveyResponse> responses;

    // Profile fields (optional)
    @Column(name = "personal_email", length = 100)
    private String personalEmail;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "linkedin_url", length = 100)
    private String linkedinUrl;

    @Column(name = "github_url", length = 100)
    private String githubUrl;

    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl = "https://avatar.iran.liara.run/public/10";

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}