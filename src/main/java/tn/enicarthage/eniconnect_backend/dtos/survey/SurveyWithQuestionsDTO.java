package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.*;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyWithQuestionsDTO {
    private Long id;
    private String title;
    private String description;
    private Integer yearOfStudy;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Boolean isActive;
    private List<SurveyQuestionDTO> questions;
    private List<CourseDTO> courses;
}