package tn.enicarthage.eniconnect_backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.student.CreateStudentDto;
import tn.enicarthage.eniconnect_backend.dtos.request.student.UpdateStudentProfileDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;

import java.util.List;

@Service
public interface StudentService {

    StudentDto getStudentById(Long id);

    StudentDto getStudentByEmail(String email);

    StudentDto getStudentByMatricule(String matricule);

    List<StudentDto> getAllStudents();

    Page<StudentDto> getAllStudents(Pageable pageable);

    StudentDto createStudent(CreateStudentDto studentCreateDTO);

    StudentDto updateStudentProfile(Long id, UpdateStudentProfileDto studentUpdateDTO);

    void deleteStudent(Long id);

    List<StudentDto> createStudentsFromCsv(MultipartFile file);

}