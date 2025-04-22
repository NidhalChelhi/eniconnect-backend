package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Data;

import java.util.List;

@Data
public class SurveySubmissionDTO {
    private Long surveyId;
    private List<CourseResponseSubmissionDTO> responses;
    private String feedback;
}