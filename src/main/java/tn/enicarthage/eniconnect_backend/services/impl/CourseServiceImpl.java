package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.course.CreateCourseDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import tn.enicarthage.eniconnect_backend.exceptions.AlreadyExistsException;
import tn.enicarthage.eniconnect_backend.exceptions.FileProcessingException;
import tn.enicarthage.eniconnect_backend.exceptions.InvalidDataException;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;
import tn.enicarthage.eniconnect_backend.services.CourseService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "code", code));
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
            throw new AlreadyExistsException("Course", "code", createCourseDto.code());
        }

        try {
            Course course = toEntity(createCourseDto);
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid course data: " + e.getMessage());
        }
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

    @Override
    @Transactional
    public List<Course> createCoursesFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileProcessingException("CSV file is empty");
        }
        try (InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            List<CreateCourseDto> dtos = new ArrayList<>();
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                CreateCourseDto dto = new CreateCourseDto(
                        values[0].trim(), // code
                        values[1].trim(), // name
                        Speciality.valueOf(values[2].trim()), // speciality
                        Integer.parseInt(values[3].trim()), // semester
                        Integer.parseInt(values[4].trim()) // level
                );
                dtos.add(dto);
            }

            return dtos.stream()
                    .map(this::createCourse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new FileProcessingException("Failed to process CSV file: " + e.getMessage(), e);
        }
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
