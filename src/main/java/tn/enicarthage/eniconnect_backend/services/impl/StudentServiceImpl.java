package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.student.CreateStudentDto;
import tn.enicarthage.eniconnect_backend.dtos.request.student.UpdateStudentProfileDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import tn.enicarthage.eniconnect_backend.exceptions.AlreadyExistsException;
import tn.enicarthage.eniconnect_backend.exceptions.FileProcessingException;
import tn.enicarthage.eniconnect_backend.exceptions.InvalidDataException;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.mappers.StudentMapper;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;
import tn.enicarthage.eniconnect_backend.services.StudentService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto getStudentByMatricule(String matricule) {
        Student student = studentRepository.findByMatricule(matricule)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "matricule", matricule));
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
        if (studentRepository.existsByEmail(studentCreateDTO.email())) {
            throw new AlreadyExistsException("Student", "email", studentCreateDTO.email());
        }
        if (studentRepository.existsByMatricule(studentCreateDTO.matricule())) {
            throw new AlreadyExistsException("Student", "matricule", studentCreateDTO.matricule());
        }

        try {
            Student student = studentMapper.toEntity(studentCreateDTO);
            Student savedStudent = studentRepository.save(student);
            return studentMapper.toDto(savedStudent);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid student data: " + e.getMessage());
        }
    }

    @Override
    public StudentDto updateStudentProfile(Long id, UpdateStudentProfileDto studentUpdateDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        studentMapper.updateEntityFromDto(student, studentUpdateDTO);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", "id", id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<StudentDto> createStudentsFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileProcessingException("CSV file is empty");
        }

        try (InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            List<CreateStudentDto> dtos = new ArrayList<>();
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                CreateStudentDto dto = new CreateStudentDto(
                        values[0].trim(), // matricule
                        values[1].trim(), // firstName
                        values[2].trim(), // lastName
                        values[3].trim(), // email
                        Speciality.valueOf(values[4].trim()), // speciality
                        Integer.parseInt(values[5].trim()), // currentLevel
                        values[6].trim(), // groupe
                        values[7].trim(), // entrySchoolYear
                        values.length > 8 ? values[8].trim() : null // gender
                );
                dtos.add(dto);
            }

            return dtos.stream()
                    .map(this::createStudent)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new FileProcessingException("Failed to process CSV file: " + e.getMessage(), e);
        }
    }
}
