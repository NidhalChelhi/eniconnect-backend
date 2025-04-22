package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.AcademicYearDTO;
import tn.enicarthage.eniconnect_backend.repositories.AcademicYearRepository;
import tn.enicarthage.eniconnect_backend.services.AcademicYearService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {
    private final AcademicYearRepository academicYearRepository;

    @Override
    public List<AcademicYearDTO> getAllAcademicYears() {
        return academicYearRepository.findAll().stream()
                .map(year -> AcademicYearDTO.builder()
                        .id(year.getId())
                        .name(year.getName())
                        .startYear(year.getStartYear())
                        .endYear(year.getEndYear())
                        .build())
                .collect(Collectors.toList());
    }
}