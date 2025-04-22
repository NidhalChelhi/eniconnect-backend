package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.AcademicYearDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.SemesterDTO;
import tn.enicarthage.eniconnect_backend.repositories.SemesterRepository;
import tn.enicarthage.eniconnect_backend.services.SemesterService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final SemesterRepository semesterRepository;

    @Override
    public List<SemesterDTO> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .map(semester -> SemesterDTO.builder()
                        .id(semester.getId())
                        .number(semester.getNumber())
                        .startDate(semester.getStartDate())
                        .endDate(semester.getEndDate())
                        .academicYear(AcademicYearDTO.builder()
                                .id(semester.getAcademicYear().getId())
                                .name(semester.getAcademicYear().getName())
                                .startYear(semester.getAcademicYear().getStartYear())
                                .endYear(semester.getAcademicYear().getEndYear())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}