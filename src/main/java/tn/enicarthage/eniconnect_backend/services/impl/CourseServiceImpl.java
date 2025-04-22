package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.services.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setId(course.getId());
                    courseDTO.setName(course.getName());
                    courseDTO.setCode(course.getCode());
                    courseDTO.setDescription(course.getDescription());
                    return courseDTO;
                })
                .toList();
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    CourseDTO courseDTO = new CourseDTO();
                    courseDTO.setId(course.getId());
                    courseDTO.setName(course.getName());
                    courseDTO.setCode(course.getCode());
                    courseDTO.setDescription(course.getDescription());
                    return courseDTO;
                })
                .orElse(null);
    }


}
