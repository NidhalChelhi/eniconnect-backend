package tn.enicarthage.eniconnect_backend.services;


import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public interface SurveyService {
    SurveyDto getSurveyById(Long id);
    List<SurveyDto> getAllSurveys();
    Page<SurveyDto> getAllSurveys(Pageable pageable);
    SurveyDto createSurvey(CreateSurveyDto CreateSurveyDto);
    void deleteSurvey(Long id);


}
