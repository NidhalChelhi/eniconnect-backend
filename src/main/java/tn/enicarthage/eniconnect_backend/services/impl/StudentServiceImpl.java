package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.StudentDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.StudentService;
import tn.enicarthage.eniconnect_backend.utils.AcademicUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SurveyRepository surveyRepository;
    private final SpecializationRepository specializationRepository;
    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    @Override
    public StudentDTO getStudentByMatricule(String matricule) {
        Student student = studentRepository.findByMatricule(matricule);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with matricule: " + matricule);
        }
        return convertToDTO(student);
    }

    @Override
    public List<StudentDTO> getStudentsBySpecialization(String specializationCode) {
        Specialization specialization = specializationRepository.findByCode(specializationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
        return studentRepository.findBySpecialization(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return convertToDTO(student);
    }

    @Override
    public List<SurveyDTO> getSurveysForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));


        return surveyRepository.findBySpecializationAndYearOfStudy(
                        student.getSpecialization(),
                        AcademicUtils.calculateYearOfStudy(student.getEntryYear()))
                .stream()
                .map(this::convertSurveyToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .matricule(student.getMatricule())
                .specializationCode(student.getSpecialization().getCode())
                .specializationName(student.getSpecialization().getName())
                .yearOfStudy(
                        AcademicUtils.calculateYearOfStudy(student.getEntryYear()) // Calculate year of study
                )
                .email(student.getUser().getEmail())
                .build();
    }

    private SurveyDTO convertSurveyToDTO(Survey survey) {
        return SurveyDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .yearOfStudy(survey.getYearOfStudy())
                .openDate(survey.getOpenDate())
                .closeDate(survey.getCloseDate())
                .isActive(survey.getIsActive())
                .build();
    }
}