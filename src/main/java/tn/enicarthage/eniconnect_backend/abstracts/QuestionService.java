package tn.enicarthage.eniconnect_backend.abstracts;

import tn.enicarthage.eniconnect_backend.dtos.QuestionCreateDTO;
import tn.enicarthage.eniconnect_backend.entities.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    List<Question> findAll();

    Question findOne(UUID questionId);

    Question createOne(QuestionCreateDTO questionCreateDTO);

    void deleteOne(UUID questionId);
}
