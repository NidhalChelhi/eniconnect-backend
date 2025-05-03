package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.course.CreateCourseDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.services.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Course not found")
        );
        return course;
    }

    @Override
    public Course getCourseByCode(String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(
                () -> new RuntimeException("Course not found")
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

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Course not found")
        );
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
