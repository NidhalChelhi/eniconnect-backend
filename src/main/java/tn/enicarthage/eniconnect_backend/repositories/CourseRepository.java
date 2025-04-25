package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCode(String code);

    List<Course> findByCodeIn(List<String> codes);

}