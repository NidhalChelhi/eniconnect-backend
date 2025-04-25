package tn.enicarthage.eniconnect_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course_responses")
public class CourseResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_response_id", nullable = false)
    private SurveyResponse surveyResponse;

    @ManyToOne
    @JoinColumn(name = "semester_course_id", nullable = false)
    private SemesterCourse semesterCourse;

    @Column(name = "response_order") // Changed from 'order' to avoid SQL keyword conflict
    private Integer order;

    @OneToMany(mappedBy = "courseResponse", cascade = CascadeType.ALL)
    private List<QuestionResponse> questionResponses;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}