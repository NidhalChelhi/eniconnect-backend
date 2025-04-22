package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Data;

import java.util.List;

@Data
public class SurveyTemplateDTO {
    private Long id;
    private String name;
    private String description;
    private List<SurveyQuestionDTO> questions;
}