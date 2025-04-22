package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class CourseResponseSubmissionDTO {
    private Long semesterCourseId;
    private Integer order;  // Added this field
    private List<QuestionResponseSubmissionDTO> answers;
}