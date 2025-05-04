package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.course.CreateCourseDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;
import tn.enicarthage.eniconnect_backend.services.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SurveyRepository surveyRepository;

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    @Override
    public Course getCourseByCode(String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Course", "code", code)
        );
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course createCourse(CreateCourseDto createCourseDto) {
        if (courseRepository.existsByCode(createCourseDto.code())) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = toEntity(createCourseDto);
        return courseRepository.save(course);
    }

    // In CourseServiceImpl
    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        // Remove course from all surveys that reference it
        List<Survey> surveys = surveyRepository.findAllByTargetCoursesContaining(course);
        surveys.forEach(survey -> survey.getTargetCourses().remove(course));

        courseRepository.delete(course);
    }


    public Course toEntity(CreateCourseDto createCourseDto) {
        return Course.builder()
                .code(createCourseDto.code())
                .name(createCourseDto.name())
                .speciality(createCourseDto.speciality())
                .semester(createCourseDto.semester())
                .level(createCourseDto.level())
                .build();

    }
}
