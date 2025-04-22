package tn.enicarthage.eniconnect_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.abstracts.SurveyQuestionService;
import tn.enicarthage.eniconnect_backend.abstracts.SurveyService;
import tn.enicarthage.eniconnect_backend.dtos.SurveyCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.SurveyResponseDTO;
import tn.enicarthage.eniconnect_backend.dtos.QuestionDTO;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.entities.SurveyQuestion;
import tn.enicarthage.eniconnect_backend.entities.Question;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyQuestionRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final CourseRepository courseRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyQuestionService surveyQuestionService;

    public SurveyServiceImpl(SurveyRepository surveyRepository, CourseRepository courseRepository, SurveyQuestionRepository surveyQuestionRepository, SurveyQuestionService surveyQuestionService) {
        this.surveyRepository = surveyRepository;
        this.courseRepository = courseRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyQuestionService = surveyQuestionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyResponseDTO> findAll() {
        return surveyRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyResponseDTO findOne(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey with id " + surveyId + " not found"));
        return convertToDTO(survey);
    }

    @Override
    @Transactional
    public SurveyResponseDTO createOne(SurveyCreateDTO surveyCreateDTO) {
        // Validate dates
        if (surveyCreateDTO.openDate().isAfter(surveyCreateDTO.closeDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Open date must be before close date");
        }

        // Create and save survey
        Survey survey = new Survey();
        survey.setTitle(surveyCreateDTO.title());
        survey.setFiliere(surveyCreateDTO.filiere());
        survey.setSemester(surveyCreateDTO.semester());
        survey.setOpenDate(surveyCreateDTO.openDate());
        survey.setCloseDate(surveyCreateDTO.closeDate());

        // Get courses by filiere and semester
        List<Course> courses = courseRepository.findAllByFiliereAndSemester(surveyCreateDTO.filiere(), surveyCreateDTO.semester());
        survey.setCourses(courses);

        survey = surveyRepository.save(survey);

        // Add questions to survey
        surveyQuestionService.addQuestionsToSurvey(survey.getId(), surveyCreateDTO.questions());

        // Return the complete DTO
        return convertToDTO(survey);
    }

    @Override
    @Transactional
    public void deleteOne(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey with id " + surveyId + " not found"));
        surveyRepository.delete(survey);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSurveyOpen(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey with id " + surveyId + " not found"));

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(survey.getOpenDate()) && now.isBefore(survey.getCloseDate());
    }

    // Helper method to convert Entity to DTO
    private SurveyResponseDTO convertToDTO(Survey survey) {
        List<SurveyQuestionDTO> questionDTOs = surveyQuestionRepository.findBySurveyIdOrderByDisplayOrderAsc(survey.getId()).stream().map(this::convertSurveyQuestionToDTO).collect(Collectors.toList());

        return new SurveyResponseDTO(survey.getId(), survey.getTitle(), survey.getFiliere(), survey.getSemester(), survey.getOpenDate(), survey.getCloseDate(), survey.getCourses(), // Using Course entity directly since no recursion
                questionDTOs, survey.getCreatedAt());
    }

    // Helper method to convert SurveyQuestion to DTO
    private SurveyQuestionDTO convertSurveyQuestionToDTO(SurveyQuestion eq) {
        return new SurveyQuestionDTO(eq.getId(), convertQuestionToDTO(eq.getQuestion()), eq.getDisplayOrder(), eq.getCreatedAt());
    }

    // Helper method to convert Question to DTO
    private QuestionDTO convertQuestionToDTO(Question question) {
        return new QuestionDTO(question.getId(), question.getText(), question.getType(), question.getOptions(), question.getCreatedAt());
    }
}
