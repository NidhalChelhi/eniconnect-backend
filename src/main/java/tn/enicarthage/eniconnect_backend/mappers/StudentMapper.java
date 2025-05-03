package tn.enicarthage.eniconnect_backend.mappers;

import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.dtos.request.student.CreateStudentDto;
import tn.enicarthage.eniconnect_backend.dtos.request.student.UpdateStudentProfileDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.entities.Student;

@Component
public class StudentMapper {
    public StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getMatricule(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getSpeciality(),
                student.getCurrentLevel(),
                student.getGroupe(),
                student.getEntrySchoolYear(),
                student.getGraduationSchoolYear(),
                student.getGender(),
                student.getPersonalEmail(),
                student.getPhoneNumber(),
                student.getBio(),
                student.getLinkedinUrl(),
                student.getGithubUrl(),
                student.getProfilePictureUrl(),
                student.getCreatedAt()

        );
    }

    public Student toEntity(CreateStudentDto createStudentDto) {
        Student student = new Student();
        student.setMatricule(createStudentDto.matricule());
        student.setFirstName(createStudentDto.firstName());
        student.setLastName(createStudentDto.lastName());
        student.setEmail(createStudentDto.email());
        student.setSpeciality(createStudentDto.speciality());
        student.setCurrentLevel(createStudentDto.currentLevel());
        student.setGroupe(createStudentDto.groupe());
        student.setEntrySchoolYear(createStudentDto.entrySchoolYear());
        student.setGender(createStudentDto.gender());
        return student;
    }


    public Student updateEntityFromDto(Student student, UpdateStudentProfileDto updateStudentProfileDto) {
        student.setPhoneNumber(updateStudentProfileDto.phoneNumber());
        student.setPersonalEmail(updateStudentProfileDto.personalEmail());
        student.setBio(updateStudentProfileDto.bio());
        student.setLinkedinUrl(updateStudentProfileDto.linkedinUrl());
        student.setGithubUrl(updateStudentProfileDto.githubUrl());
        student.setProfilePictureUrl(updateStudentProfileDto.profilePictureUrl());
        return student;
    }

}
