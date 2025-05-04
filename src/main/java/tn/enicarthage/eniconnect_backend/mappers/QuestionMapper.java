package tn.enicarthage.eniconnect_backend.mappers;

import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.dtos.request.question.QuestionTemplateDto;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;

@Component
public class QuestionMapper {
    public QuestionTemplate toEntity(QuestionTemplateDto dto) {
            QuestionTemplate question = new QuestionTemplate();
            question.setText(dto.text());
            question.setMaxValue(dto.maxValue());
            question.setMinValue(dto.minValue());
            return question;
    } ;
}
