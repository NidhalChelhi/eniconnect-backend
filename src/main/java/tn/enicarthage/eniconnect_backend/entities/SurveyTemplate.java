package tn.enicarthage.eniconnect_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey_templates")
public class SurveyTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "surveyTemplate")
    @JsonIgnore
    private List<SurveyQuestion> questions;


    @Column(nullable = false)
    private Boolean isSystemDefault = false; // For pre-built templates


    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}