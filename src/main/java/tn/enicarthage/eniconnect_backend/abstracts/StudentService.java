package tn.enicarthage.eniconnect_backend.abstracts;

import tn.enicarthage.eniconnect_backend.dtos.StudentCreate;
import tn.enicarthage.eniconnect_backend.dtos.StudentUpdate;
import tn.enicarthage.eniconnect_backend.entities.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    Student findOne(UUID studentId);

    List<Student> findAll();

    void deleteOne(UUID studentId);

    Student updateOne(UUID studentId, StudentUpdate student);

    Student createOne(StudentCreate student);
}
