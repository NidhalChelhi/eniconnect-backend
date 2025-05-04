package tn.enicarthage.eniconnect_backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.course.CreateCourseDto;
import tn.enicarthage.eniconnect_backend.entities.Course;

import java.util.List;

@Service
public interface CourseService {

    Course getCourseById(Long id);

    Course getCourseByCode(String code);

    List<Course> getAllCourses();

    Page<Course> getAllCourses(Pageable pageable);

    Course createCourse(CreateCourseDto createCourseDto);

    void deleteCourse(Long id);

    List<Course> createCoursesFromCsv(MultipartFile file);

}