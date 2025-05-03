package tn.enicarthage.eniconnect_backend.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import tn.enicarthage.eniconnect_backend.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final QuestionTemplateRepository questionTemplateRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            seedCourses();
            seedStudents();
            seedQuestions();
            seedSurveys();
        }
    }

    private void seedCourses() {
        List<Course> courses = List.of(
                // Level 1 - Semester 1 (GI)
                createCourse("INF101", "Algorithmique", Speciality.INFO, 1, 1),
                createCourse("INF102", "Systèmes d'Information", Speciality.INFO, 1, 1),
                createCourse("INF103", "Mathématiques Discrètes", Speciality.INFO, 1, 1),

                // Level 1 - Semester 2 (GI)
                createCourse("INF201", "Structures de Données", Speciality.INFO, 2, 1),
                createCourse("INF202", "Bases de Données", Speciality.INFO, 2, 1),

                // Level 2 - Semester 1 (GI)
                createCourse("INF301", "Réseaux", Speciality.INFO, 1, 2),
                createCourse("INF302", "Systèmes d'Exploitation", Speciality.INFO, 1, 2),

                // Other specialities (sample)
                createCourse("GSI101", "Électronique Numérique", Speciality.GSI, 1, 1),
                createCourse("MECA201", "Mécanique des Solides", Speciality.MECA, 2, 1),
                createCourse("GSIL101", "Logistique Industrielle", Speciality.GSIL, 1, 1)
        );

        courseRepository.saveAll(courses);
    }

    private void seedStudents() {
        List<Student> students = IntStream.rangeClosed(1, 50)
                .mapToObj(i -> {
                    Speciality speciality = getRandomSpeciality();
                    String groupe = String.valueOf((char) ('A' + ThreadLocalRandom.current().nextInt(4)));
                    String gender = ThreadLocalRandom.current().nextBoolean() ? "male" : "female";
                    int avatarId = ThreadLocalRandom.current().nextInt(1, 51); // Random avatar 1-50

                    return Student.builder()
                            .matricule(generateMatricule(i, speciality))
                            .firstName("Prénom" + i)
                            .lastName("Nom" + i)
                            .email("etudiant" + i + "@enicar.ucar.tn")
                            .speciality(speciality)
                            .currentLevel(1)
                            .groupe(groupe)
                            .entrySchoolYear("2023/2024")
                            .gender(gender)
                            .phoneNumber("+216" + (20000000 + i))
                            .profilePictureUrl("https://avatar.iran.liara.run/public/" + avatarId)
                            .build();
                })
                .toList();

        studentRepository.saveAll(students);
    }

    private void seedQuestions() {
        List<QuestionTemplate> questions = List.of(
                createQuestion("Les objectifs visés et les compétences ciblées pour ce module sont clairement annoncés dès le début", 1),
                createQuestion("Le contenu est en adéquation avec les objectifs et les compétences annoncés", 2),
                createQuestion("Le contenu du cours est adapté aux attentes et exigences actuelles", 3),
                createQuestion("Les supports utilisés (Diapos, polycopiés, …) sont de bonnes qualités au niveau de la forme et du fond", 4),
                createQuestion("L'organisation du module, le volume horaire, le rythme et l'ambiance sont satisfaisants", 5),
                createQuestion("La méthode d'évaluation (contrôle continu, examens, projets,…) est bien adaptée", 6),
                createQuestion("Les Compétences visées ont été atteintes par vous élève-ingénieur", 7),
                createQuestion("Avez-vous apprécié ce module ?", 8)
        );

        questionTemplateRepository.saveAll(questions);
    }

    private void seedSurveys() {
        // Create surveys for each semester
        List<Survey> surveys = List.of(
                createSurvey("Évaluation S1 2023/2024 - Génie Informatique",
                        Speciality.INFO, 1, 1, "2023/2024", true),
                createSurvey("Évaluation S2 2023/2024 - Génie Informatique",
                        Speciality.INFO, 2, 1, "2023/2024", false),
                createSurvey("Évaluation S1 2023/2024 - GSI",
                        Speciality.GSI, 1, 1, "2023/2024", true)
        );

        // Assign courses to each survey
        surveys.forEach(survey -> {
            Set<Course> courses = Set.copyOf(
                    courseRepository.findBySpecialityAndLevelAndSemester(
                            survey.getSpeciality(),
                            survey.getLevel(),
                            survey.getSemester()
                    )
            );
            survey.setTargetCourses(courses);
            surveyRepository.save(survey);
        });
    }


    // Helper methods
    private String generateMatricule(int index, Speciality speciality) {
        return "23" + speciality.name() + String.format("%03d", index);
    }

    private Course createCourse(String code, String name, Speciality speciality, int semester, int level) {
        return Course.builder()
                .code(code)
                .name(name)
                .speciality(speciality)
                .semester(semester)
                .level(level)
                .build();
    }

    private QuestionTemplate createQuestion(String text, int order) {
        return QuestionTemplate.builder()
                .text(text)
                .orderIndex(order)
                .build();
    }

    private Survey createSurvey(String title, Speciality speciality, int semester,
                                int level, String schoolYear, boolean isPublished) {
        return Survey.builder()
                .title(title)
                .speciality(speciality)
                .semester(semester)
                .level(level)
                .schoolYear(schoolYear)
                .isPublished(isPublished)
                .openDate(LocalDateTime.now().minusDays(7))
                .closeDate(isPublished ? LocalDateTime.now().plusDays(7) : null)
                .build();
    }

    private Speciality getRandomSpeciality() {
        Speciality[] values = Speciality.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}