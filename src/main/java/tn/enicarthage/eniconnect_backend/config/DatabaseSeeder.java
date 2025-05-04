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
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.repositories.QuestionTemplateRepository;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final QuestionTemplateRepository questionTemplateRepository;
    private final SurveyRepository surveyRepository;

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
                    int avatarId = ThreadLocalRandom.current().nextInt(1, 51);

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
        // Get all courses grouped by speciality, level and semester
        List<Course> infoL1S1Courses = courseRepository.findBySpecialityAndLevelAndSemester(Speciality.INFO, 1, 1);
        List<Course> infoL1S2Courses = courseRepository.findBySpecialityAndLevelAndSemester(Speciality.INFO, 1, 2);
        List<Course> infoL2S1Courses = courseRepository.findBySpecialityAndLevelAndSemester(Speciality.INFO, 2, 1);
        List<Course> gsiL1S1Courses = courseRepository.findBySpecialityAndLevelAndSemester(Speciality.GSI, 1, 1);
        List<Course> mecaL1S2Courses = courseRepository.findBySpecialityAndLevelAndSemester(Speciality.MECA, 1, 2);

        LocalDateTime now = LocalDateTime.now();

        List<Survey> surveys = List.of(
                // 1. Published survey currently active (no close date)
                createSurvey(
                        "Évaluation Semestre 1 - INFO 1ère année",
                        Speciality.INFO,
                        1,
                        1,
                        "2023/2024",
                        true,
                        now.minusDays(7),
                        null,
                        infoL1S1Courses
                ),

                // 2. Published survey currently active with close date in future
                createSurvey(
                        "Évaluation Semestre 2 - INFO 1ère année",
                        Speciality.INFO,
                        2,
                        1,
                        "2023/2024",
                        true,
                        now.minusDays(3),
                        now.plusDays(7),
                        infoL1S2Courses
                ),

                // 3. Published survey that has closed
                createSurvey(
                        "Évaluation Semestre 1 - INFO 2ème année (2022/2023)",
                        Speciality.INFO,
                        1,
                        2,
                        "2022/2023",
                        true,
                        now.minusMonths(3),
                        now.minusMonths(2),
                        infoL2S1Courses
                ),

                // 4. Published survey not yet open
                createSurvey(
                        "Évaluation Semestre 1 - GSI 1ère année",
                        Speciality.GSI,
                        1,
                        1,
                        "2023/2024",
                        true,
                        now.plusDays(5),
                        now.plusDays(12),
                        gsiL1S1Courses
                ),

                // 5. Unpublished survey (draft)
                createSurvey(
                        "Évaluation Semestre 2 - MECA 1ère année (Draft)",
                        Speciality.MECA,
                        2,
                        1,
                        "2023/2024",
                        false,
                        now.plusDays(10),
                        now.plusDays(20),
                        mecaL1S2Courses
                ),

                // 6. Published survey with no dates (always active)
                createSurvey(
                        "Feedback Général - INFO 1ère année",
                        Speciality.INFO,
                        1,
                        1,
                        "2023/2024",
                        true,
                        null,
                        null,
                        infoL1S1Courses
                ),

                // 7. Survey from previous school year
                createSurvey(
                        "Évaluation Semestre 1 - INFO 1ère année (2022/2023)",
                        Speciality.INFO,
                        1,
                        1,
                        "2022/2023",
                        true,
                        now.minusYears(1),
                        now.minusYears(1).plusDays(14),
                        infoL1S1Courses
                ),

                // 8. Survey for next semester (planned)
                createSurvey(
                        "Évaluation Semestre 1 - INFO 1ère année (2024/2025)",
                        Speciality.INFO,
                        1,
                        1,
                        "2024/2025",
                        false,
                        now.plusMonths(6),
                        now.plusMonths(6).plusDays(14),
                        infoL1S1Courses
                )
        );

        surveyRepository.saveAll(surveys);
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

    private Survey createSurvey(
            String title,
            Speciality speciality,
            int semester,
            int level,
            String schoolYear,
            boolean isPublished,
            LocalDateTime openDate,
            LocalDateTime closeDate,
            List<Course> courses) {

        return Survey.builder()
                .title(title)
                .speciality(speciality)
                .semester(semester)
                .level(level)
                .schoolYear(schoolYear)
                .isPublished(isPublished)
                .openDate(openDate)
                .closeDate(closeDate)
                .targetCourses(new HashSet<>(courses))
                .responses(List.of())
                .build();
    }

    private Speciality getRandomSpeciality() {
        Speciality[] values = Speciality.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}