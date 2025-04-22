package tn.enicarthage.eniconnect_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.abstracts.CourseService;
import tn.enicarthage.eniconnect_backend.dtos.CourseCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.CourseUpdateDTO;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SurveyRepository surveyRepository;

    public CourseServiceImpl(CourseRepository courseRepository, SurveyRepository surveyRepository) {
        this.courseRepository = courseRepository;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findOne(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Course with id " + courseId + " not found"
        ));
    }

    @Override
    public Course createOne(CourseCreateDTO courseCreateDTO) {
        Course course = new Course();

        course.setCourseCode(courseCreateDTO.courseCode());
        course.setCourseName(courseCreateDTO.courseName());
        course.setFiliere(courseCreateDTO.filiere());
        course.setSemester(courseCreateDTO.semester());

        courseRepository.save(course);
        return course;
    }

    @Override
    public Course updateOne(UUID courseId, CourseUpdateDTO courseUpdateDTO) {
        Course existingCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Course with id " + courseId + " not found"
        ));

        existingCourse.setCourseCode(courseUpdateDTO.courseCode());
        existingCourse.setCourseName(courseUpdateDTO.courseName());
        existingCourse.setFiliere(courseUpdateDTO.filiere());
        existingCourse.setSemester(courseUpdateDTO.semester());

        courseRepository.save(existingCourse);

        return existingCourse;
    }

    @Override
    public void deleteOne(UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        // Remove course from all surveys first
        List<Survey> surveys = surveyRepository.findByCoursesContaining(course);
        for (Survey eval : surveys) {
            eval.getCourses().remove(course);
            surveyRepository.save(eval);
        }

        courseRepository.delete(course);
    }
}
