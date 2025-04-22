package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;

import java.util.List;

public interface CourseService {

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

}