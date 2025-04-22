package tn.enicarthage.eniconnect_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.abstracts.SurveyQuestionService;
import tn.enicarthage.eniconnect_backend.dtos.*;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.repositories.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyQuestionServiceImpl implements SurveyQuestionService {

    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;

    public SurveyQuestionServiceImpl(SurveyQuestionRepository surveyQuestionRepository, SurveyRepository surveyRepository, QuestionRepository questionRepository) {
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyQuestionDTO> findBySurveyId(UUID surveyId) {
        return surveyQuestionRepository.findBySurveyIdOrderByDisplayOrderAsc(surveyId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SurveyQuestionDTO> addQuestionsToSurvey(UUID surveyId, List<QuestionInSurveyDTO> questions) {
        // Validate all questions first
        questions.forEach(q -> {
            if (surveyQuestionRepository.existsBySurveyIdAndQuestionId(surveyId, q.questionId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Question with id " + q.questionId() + " already exists in survey with id " + surveyId
                );
            }
        });

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Survey with id " + surveyId + " not found"
        ));

        return questions.stream().map(q -> {
            Question question = questionRepository.findById(q.questionId()).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Question with id " + q.questionId() + " not found"
                    ));

            SurveyQuestion eq = new SurveyQuestion();
            eq.setSurvey(survey);
            eq.setQuestion(question);
            eq.setDisplayOrder(q.displayOrder());

            SurveyQuestion saved = surveyQuestionRepository.save(eq);
            return convertToDTO(saved);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeQuestionsFromSurvey(UUID surveyId, List<UUID> questionIds) {
        if (!surveyRepository.existsById(surveyId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Survey with id " + surveyId + " not found"
            );
        }
        surveyQuestionRepository.deleteBySurveyIdAndQuestionIdIn(surveyId, questionIds);
    }

    private SurveyQuestionDTO convertToDTO(SurveyQuestion eq) {
        return new SurveyQuestionDTO(eq.getId(), convertQuestionToDTO(eq.getQuestion()), eq.getDisplayOrder(), eq.getCreatedAt());
    }

    private QuestionDTO convertQuestionToDTO(Question question) {
        return new QuestionDTO(question.getId(), question.getText(), question.getType(), question.getOptions(), question.getCreatedAt());
    }
}
