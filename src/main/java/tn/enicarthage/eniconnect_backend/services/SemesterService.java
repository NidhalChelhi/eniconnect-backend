package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.base.SemesterDTO;
import java.util.List;

public interface SemesterService {
    List<SemesterDTO> getAllSemesters();
}