package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponseDTO {
    private Long id;
    private Long questionId;  // Changed from SurveyQuestionDTO to Long
    private String questionText;
    private String responseValue;
}