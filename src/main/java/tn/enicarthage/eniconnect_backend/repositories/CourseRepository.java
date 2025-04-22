package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCode(String code);

}