package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.base.SpecializationDTO;
import java.util.List;

public interface SpecializationService {
    List<SpecializationDTO> getAllSpecializations();
}