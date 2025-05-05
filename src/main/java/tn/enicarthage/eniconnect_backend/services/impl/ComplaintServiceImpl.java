package tn.enicarthage.eniconnect_backend.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.complaint.CreateComplaintDto;
import tn.enicarthage.eniconnect_backend.dtos.response.complaint.ComplaintDto;
import tn.enicarthage.eniconnect_backend.entities.Complaint;
import tn.enicarthage.eniconnect_backend.enums.ComplaintStatus;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.mappers.ComplaintMapper;
import tn.enicarthage.eniconnect_backend.repositories.ComplaintRepository;
import tn.enicarthage.eniconnect_backend.services.ComplaintService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;

    @Override
    public List<ComplaintDto> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Override
    public Page<ComplaintDto> getAllComplaints(Pageable pageable) {
        return complaintRepository.findAll(pageable)
                .map(complaintMapper::toDto);
    }

    @Override
    public ComplaintDto getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
        return complaintMapper.toDto(complaint);
    }


    @Override
    public List<ComplaintDto> getComplaintsByStudentId(Long studentId) {
        return complaintRepository.findByStudentId(studentId).stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Override
    public ComplaintDto createComplaint(CreateComplaintDto createComplaintDto) {
        Complaint complaint = complaintMapper.mapToEntity(createComplaintDto);
        complaint.setStatus(ComplaintStatus.PENDING);
        complaint.setCreatedAt(java.time.LocalDateTime.now());
        Complaint savedComplaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(savedComplaint);
    }

    @Override
    public ComplaintDto resolveComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolvedAt(java.time.LocalDateTime.now());
        Complaint resolvedComplaint = complaintRepository.save(complaint);
        return complaintMapper.toDto(resolvedComplaint);
    }

    @Override
    public void deleteComplaint(Long id) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
        complaintRepository.delete(complaint);
    }


}
