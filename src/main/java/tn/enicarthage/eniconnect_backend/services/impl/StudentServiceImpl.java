package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.student.CreateStudentDto;
import tn.enicarthage.eniconnect_backend.dtos.request.student.UpdateStudentProfileDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.mappers.StudentMapper;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;
import tn.enicarthage.eniconnect_backend.services.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto getStudentByMatricule(String phoneNumber) {
        Student student = studentRepository.findByMatricule(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        Page<Student> students = studentRepository.findAll(pageable);
        return students.map(studentMapper::toDto);
    }

    @Override
    public StudentDto createStudent(CreateStudentDto studentCreateDTO) {
        if (studentRepository.existsByEmail(studentCreateDTO.email()) || studentRepository.existsByMatricule(studentCreateDTO.matricule())) {
            throw new RuntimeException("Student already exists");
        }

        Student student = studentMapper.toEntity(studentCreateDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Override
    public StudentDto updateStudentProfile(Long id, UpdateStudentProfileDto studentUpdateDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        studentMapper.updateEntityFromDto(student, studentUpdateDTO);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }
}
