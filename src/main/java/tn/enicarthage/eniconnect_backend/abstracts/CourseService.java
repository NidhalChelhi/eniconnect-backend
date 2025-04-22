package tn.enicarthage.eniconnect_backend.abstracts;

import tn.enicarthage.eniconnect_backend.dtos.CourseCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.CourseUpdateDTO;
import tn.enicarthage.eniconnect_backend.entities.Course;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<Course> findAll();

    Course findOne(UUID courseId);

    Course createOne(CourseCreateDTO courseCreateDTO);

    Course updateOne(UUID courseId, CourseUpdateDTO courseUpdateDTO);

    void deleteOne(UUID courseId);

}
