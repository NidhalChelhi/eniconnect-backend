package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;
import tn.enicarthage.eniconnect_backend.dtos.base.StudentDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SurveyResponseDTO {
    private Long id;
    private SurveyDTO survey;
    private StudentDTO student;
    private LocalDateTime submittedAt;
    private Boolean isComplete;
    private List<CourseResponseDTO> courseResponses;
    private String feedback;
}