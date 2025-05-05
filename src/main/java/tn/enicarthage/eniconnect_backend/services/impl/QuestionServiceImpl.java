package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.question.QuestionTemplateDto;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;
import tn.enicarthage.eniconnect_backend.exceptions.InvalidDataException;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.mappers.QuestionMapper;
import tn.enicarthage.eniconnect_backend.repositories.QuestionTemplateRepository;
import tn.enicarthage.eniconnect_backend.services.QuestionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionTemplateRepository questionTemplateRepository;


    @Override
    public List<QuestionTemplate> getAllQuestions() {

        return questionTemplateRepository.findAll();
    }

    @Override
    public QuestionTemplate createQuestion(QuestionTemplateDto question) {
        int order = (int) questionTemplateRepository.count();

        QuestionTemplate questionTemplate = questionMapper.toEntity(question);
        questionTemplate.setOrderIndex(order);
        return questionTemplateRepository.save(questionTemplate);
    }

    @Override
    public QuestionTemplate updateeQuestionById(long id, QuestionTemplateDto question) {
        QuestionTemplate questionTemplate = questionTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        try {
            questionTemplate.setText(question.text());
            questionTemplate.setMaxValue(question.maxValue());
            questionTemplate.setMinValue(question.minValue());
            return questionTemplateRepository.save(questionTemplate);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid question data: " + e.getMessage());
        }
    }

    @Override
    public void DeleteQuestion(Long id) {
        if (!questionTemplateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionTemplateRepository.deleteById(id);
    }
}
