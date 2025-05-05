package tn.enicarthage.eniconnect_backend.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.complaint.CreateComplaintDto;
import tn.enicarthage.eniconnect_backend.dtos.response.complaint.ComplaintDto;

import java.util.List;

@Service
public interface ComplaintService {

    List<ComplaintDto> getAllComplaints();

    Page<ComplaintDto> getAllComplaints(Pageable pageable);

    ComplaintDto getComplaintById(Long id);

    List<ComplaintDto> getComplaintsByStudentId(Long studentId);

    ComplaintDto createComplaint(CreateComplaintDto createComplaintDto);

    ComplaintDto resolveComplaint(Long id);

    void deleteComplaint(Long id);

}