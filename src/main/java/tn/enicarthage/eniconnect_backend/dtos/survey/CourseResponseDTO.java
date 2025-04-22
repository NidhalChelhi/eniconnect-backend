package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CourseResponseDTO {
    private Long id;
    private String courseName;  // Changed to match what we're building
    private List<QuestionResponseDTO> responses;
}