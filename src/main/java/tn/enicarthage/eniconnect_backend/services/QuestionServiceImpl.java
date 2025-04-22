package tn.enicarthage.eniconnect_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.abstracts.QuestionService;
import tn.enicarthage.eniconnect_backend.dtos.QuestionCreateDTO;
import tn.enicarthage.eniconnect_backend.entities.Question;
import tn.enicarthage.eniconnect_backend.enums.QuestionType;
import tn.enicarthage.eniconnect_backend.repositories.QuestionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public Question findOne(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Question with id " + questionId + " not found"
                ));
    }

    @Override
    public Question createOne(QuestionCreateDTO questionCreateDTO) {
        if (questionCreateDTO.type() == QuestionType.LIKERT_4) {
            if (questionCreateDTO.options() == null || questionCreateDTO.options().size() != 4) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Question of type LIKERT_4 must have exactly 4 options"
                );
            }
        }

        Question question = new Question();
        question.setText(questionCreateDTO.text());
        question.setType(questionCreateDTO.type());
        question.setOptions(questionCreateDTO.options());

        return questionRepository.save(question);
    }

    @Override
    public void deleteOne(UUID questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Question with id " + questionId + " not found"
            );
        }
        questionRepository.deleteById(questionId);
    }
}
