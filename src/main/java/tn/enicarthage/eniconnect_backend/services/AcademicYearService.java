package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.base.AcademicYearDTO;
import java.util.List;

public interface AcademicYearService {
    List<AcademicYearDTO> getAllAcademicYears();
}