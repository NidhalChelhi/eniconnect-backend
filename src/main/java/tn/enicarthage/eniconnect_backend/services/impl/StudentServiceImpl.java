package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.StudentDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.entities.Specialization;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.SemesterCourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.SpecializationRepository;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;
import tn.enicarthage.eniconnect_backend.services.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final SpecializationRepository specializationRepository;
    private final SurveyRepository surveyRepository;
    private final SemesterCourseRepository semesterCourseRepository;

    @Override
    public StudentDTO getStudentById(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        Student student = studentOptional.orElseThrow(() ->
                new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    @Override
    public StudentDTO getStudentByMatricule(String matricule) {
        Optional<Student> studentOptional = Optional.ofNullable(studentRepository.findByMatricule(matricule));
        Student student = studentOptional.orElseThrow(() ->
                new ResourceNotFoundException("Student not found with matricule: " + matricule));
        return convertToDTO(student);
    }

    @Override
    public List<StudentDTO> getStudentsBySpecialization(String specializationCode) {
        Optional<Specialization> specOptional = (specializationRepository.findByCode(specializationCode));
        Specialization specialization = specOptional.orElseThrow(() ->
                new ResourceNotFoundException("Specialization not found"));

        return studentRepository.findBySpecialization(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .matricule(student.getMatricule())
                .specializationCode(student.getSpecialization().getCode())
                .build();
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
                        calculateYearOfStudy(student.getEntryYear()))
                .stream()
                .map(survey -> SurveyDTO.builder()
                        .id(survey.getId())
                        .title(survey.getTitle())
                        .yearOfStudy(survey.getYearOfStudy())
                        .isActive(survey.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }

    private int calculateYearOfStudy(int entryYear) {
        int currentYear = java.time.Year.now().getValue();
        return currentYear - entryYear + 1;
    }


}