package tn.enicarthage.eniconnect_backend.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.dtos.request.complaint.CreateComplaintDto;
import tn.enicarthage.eniconnect_backend.dtos.response.complaint.ComplaintDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.entities.Complaint;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;

@Component
@RequiredArgsConstructor
public class ComplaintMapper {

    private final StudentRepository studentRepository;


    public ComplaintDto toDto(Complaint complaint) {
        return new ComplaintDto(
                complaint.getId(),
                new StudentDto(
                        complaint.getStudent().getId(),
                        complaint.getStudent().getMatricule(),
                        complaint.getStudent().getFirstName(),
                        complaint.getStudent().getLastName(),
                        complaint.getStudent().getEmail(),
                        complaint.getStudent().getSpeciality(),
                        complaint.getStudent().getCurrentLevel(),
                        complaint.getStudent().getGroupe(),
                        complaint.getStudent().getEntrySchoolYear(),
                        complaint.getStudent().getGraduationSchoolYear(),
                        complaint.getStudent().getGender(),

                        complaint.getStudent().getPersonalEmail(),
                        complaint.getStudent().getPhoneNumber(),
                        complaint.getStudent().getBio(),
                        complaint.getStudent().getLinkedinUrl(),
                        complaint.getStudent().getGithubUrl(),
                        complaint.getStudent().getProfilePictureUrl(),
                        complaint.getStudent().getCreatedAt()
                ),
                complaint.getIsAnonymous(),
                complaint.getType().name(),
                complaint.getMessage(),
                complaint.getStatus().name(),
                complaint.getCreatedAt().toString(),
                complaint.getResolvedAt()
        );
    }

    public Complaint mapToEntity(CreateComplaintDto createComplaintDto) {
        Complaint complaint = new Complaint();
        complaint.setMessage(createComplaintDto.message());
        complaint.setType(createComplaintDto.type());
        if (createComplaintDto.studentId() != null) {
            Student student = studentRepository.findById(createComplaintDto.studentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", createComplaintDto.studentId()));
            complaint.setStudent(student);
        } else {
            complaint.setStudent(null);
        }
        complaint.setIsAnonymous(createComplaintDto.isAnonymous());
        return complaint;
    }

}