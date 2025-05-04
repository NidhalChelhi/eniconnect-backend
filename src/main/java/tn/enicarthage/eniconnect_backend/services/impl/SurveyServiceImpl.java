package tn.enicarthage.eniconnect_backend.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.mappers.StudentMapper;
import tn.enicarthage.eniconnect_backend.mappers.SurveyMapper;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {
    private SurveyRepository surveyRepository;
    private SurveyMapper surveyMapper;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper, StudentMapper studentMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
    }


    @Override
    public SurveyDto getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        return surveyMapper.toDto(survey);
    }

    public List<SurveyDto> getAllSurveys() {
        return surveyRepository.findAll().
                stream().
                map(surveyMapper::toDto).
                toList();
    }

    @Override
    public Page<SurveyDto> getAllSurveys(Pageable pageable) {
        Page<Survey> Surveys = surveyRepository.findAll(pageable) ;
        return Surveys.map(surveyMapper::toDto) ;
    }

    @Override
    public SurveyDto createSurvey(CreateSurveyDto createSurveyDto) {
        Survey survey = surveyMapper.toEntity(createSurveyDto);
        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(savedSurvey);
    }

    @Override
    public void deleteSurvey(Long id) {
        surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        surveyRepository.deleteById(id);

    }


}
