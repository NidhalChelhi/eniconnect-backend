package tn.enicarthage.eniconnect_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.abstracts.StudentService;
import tn.enicarthage.eniconnect_backend.dtos.StudentCreate;
import tn.enicarthage.eniconnect_backend.dtos.StudentUpdate;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepo;
    private final EmailService emailService;

    public Student findOne(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student with id " + studentId + " not found"
                ));
    }

    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    @Override
    public void deleteOne(UUID studentId) {
        if (!studentRepo.existsById(studentId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student with id " + studentId + " not found"
            );
        }
        studentRepo.deleteById(studentId);
    }

    public Student updateOne(UUID studentId, StudentUpdate student) {
        Student existingStudent = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student with id " + studentId + " not found"
                ));

        existingStudent.setProfilePicture(student.profilePicture());
        existingStudent.setBirthDate(student.birthDate());
        existingStudent.setPersonalEmail(student.personalEmail());
        existingStudent.setPhoneNumber(student.phoneNumber());
        existingStudent.setBio(student.bio());

        return studentRepo.save(existingStudent);
    }

    @Transactional
    public Student createOne(StudentCreate studentCreate) {
        if (studentRepo.existsByEmail(studentCreate.email())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }

        if (studentRepo.existsByCin(studentCreate.cin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CIN already exists"
            );
        }

        Student student = new Student();
        String token = UUID.randomUUID().toString();

        student.setVerified(false);
        student.setAccountCreationToken(token);
        student.setFirstName(studentCreate.firstName());
        student.setLastName(studentCreate.lastName());
        student.setEmail(studentCreate.email());
        student.setCin(studentCreate.cin());
        student.setFiliere(studentCreate.filiere());
        student.setNiveau(studentCreate.niveau());
        student.setClasse(studentCreate.classe());

        Student savedStudent = studentRepo.save(student);

        try {
            emailService.sendAccountCreationEmail(savedStudent.getEmail(), token);
        } catch (Exception emailEx) {
            System.out.println("Failed to send email: " + emailEx.getMessage());
        }

        return savedStudent;
    }
}