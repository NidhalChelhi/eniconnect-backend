package tn.enicarthage.eniconnect_backend.services;


import tn.enicarthage.eniconnect_backend.dtos.base.StudentDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;

import java.util.List;


public interface StudentService {
    StudentDTO getStudentById(Long id);

    StudentDTO getStudentByMatricule(String matricule);

    List<StudentDTO> getStudentsBySpecialization(String specializationCode);

    StudentDTO getStudentByUserId(Long userId);

    List<SurveyDTO> getSurveysForStudent(Long studentId);

}