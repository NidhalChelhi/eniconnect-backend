package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.SpecializationDTO;
import tn.enicarthage.eniconnect_backend.repositories.SpecializationRepository;
import tn.enicarthage.eniconnect_backend.services.SpecializationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {
    private final SpecializationRepository specializationRepository;

    @Override
    public List<SpecializationDTO> getAllSpecializations() {
        return specializationRepository.findAll().stream()
                .map(spec -> SpecializationDTO.builder()
                        .id(spec.getId())
                        .name(spec.getName())
                        .code(spec.getCode())
                        .build())
                .collect(Collectors.toList());
    }
}