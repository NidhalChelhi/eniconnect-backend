package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Data;

@Data
public class QuestionResponseSubmissionDTO {
    private Long questionId;
    private String response;
}