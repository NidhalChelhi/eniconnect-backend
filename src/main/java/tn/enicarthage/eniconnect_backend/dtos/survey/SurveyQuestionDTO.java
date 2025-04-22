package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyQuestionDTO {
    private Long id;
    private String questionText;
    private String questionType;
    private Integer questionOrder;
    private List<String> options; // For Likert scale options
}