package tn.enicarthage.eniconnect_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyTemplateRepository surveyTemplateRepository;
    private final SemesterCourseRepository semesterCourseRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final AcademicYearRepository academicYearRepository;
    private final StudentRepository studentRepository;
    private final SpecializationRepository specializationRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final QuestionResponseRepository questionResponseRepository;

    @Override
    public void run(String... args) {
        // Only initialize if no users exist
        if (userRepository.count() == 0) {
            // ========== SPECIALIZATIONS ==========
            Specialization gi = Specialization.builder()
                    .name("Génie Informatique")
                    .code("GI")
                    .build();

            Specialization info = Specialization.builder()
                    .name("Infotronique")
                    .code("INFO")
                    .build();

            Specialization indus = Specialization.builder()
                    .name("Génie Industriel")
                    .code("INDUS")
                    .build();

            Specialization mécat = Specialization.builder()
                    .name("Mécatronique")
                    .code("MECAT")
                    .build();

            specializationRepository.saveAll(Arrays.asList(gi, info, indus, mécat));

            // ========== ACADEMIC YEARS ==========
            AcademicYear year2022 = AcademicYear.builder()
                    .name("2022/2023")
                    .startYear(2022)
                    .endYear(2023)
                    .build();

            AcademicYear year2023 = AcademicYear.builder()
                    .name("2023/2024")
                    .startYear(2023)
                    .endYear(2024)
                    .build();

            academicYearRepository.saveAll(Arrays.asList(year2022, year2023));

            // ========== SEMESTERS ==========
            Semester s1_2022 = Semester.builder()
                    .academicYear(year2022)
                    .number(1)
                    .startDate(LocalDate.of(2022, 9, 15))
                    .endDate(LocalDate.of(2023, 1, 15))
                    .build();

            Semester s2_2022 = Semester.builder()
                    .academicYear(year2022)
                    .number(2)
                    .startDate(LocalDate.of(2023, 2, 1))
                    .endDate(LocalDate.of(2023, 6, 15))
                    .build();

            Semester s1_2023 = Semester.builder()
                    .academicYear(year2023)
                    .number(1)
                    .startDate(LocalDate.of(2023, 9, 15))
                    .endDate(LocalDate.of(2024, 1, 15))
                    .build();

            semesterRepository.saveAll(Arrays.asList(s1_2022, s2_2022, s1_2023));

            // ========== COURSES ==========
            Course ppst = Course.builder()
                    .name("Probabilité et Processus Stochastique")
                    .code("PPST")
                    .description("Module de probabilités")
                    .build();

            Course algo = Course.builder()
                    .name("Algorithmique Avancée")
                    .code("ALGO")
                    .description("Structures de données et algorithmes")
                    .build();

            Course bd = Course.builder()
                    .name("Bases de Données")
                    .code("BD")
                    .description("Modélisation et implémentation des bases de données")
                    .build();

            Course sys = Course.builder()
                    .name("Systèmes d'Exploitation")
                    .code("SYS")
                    .description("Fonctionnement des systèmes d'exploitation")
                    .build();

            courseRepository.saveAll(Arrays.asList(ppst, algo, bd, sys));

            // ========== SEMESTER COURSES ==========
            // GI Year 1
            SemesterCourse gi1_ppst = SemesterCourse.builder()
                    .semester(s1_2022)
                    .specialization(gi)
                    .course(ppst)
                    .yearOfStudy(1)
                    .build();

            SemesterCourse gi1_algo = SemesterCourse.builder()
                    .semester(s1_2022)
                    .specialization(gi)
                    .course(algo)
                    .yearOfStudy(1)
                    .build();

            // GI Year 2
            SemesterCourse gi2_bd = SemesterCourse.builder()
                    .semester(s1_2023)
                    .specialization(gi)
                    .course(bd)
                    .yearOfStudy(2)
                    .build();

            SemesterCourse gi2_sys = SemesterCourse.builder()
                    .semester(s1_2023)
                    .specialization(gi)
                    .course(sys)
                    .yearOfStudy(2)
                    .build();

            semesterCourseRepository.saveAll(Arrays.asList(gi1_ppst, gi1_algo, gi2_bd, gi2_sys));

            // ========== USERS ==========
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .email("admin@enicarthage.tn")
                    .role("ADMIN")
                    .createdAt(LocalDateTime.now())
                    .build();

            User student1 = User.builder()
                    .username("student1")
                    .passwordHash(passwordEncoder.encode("student123"))
                    .email("student1@enicarthage.tn")
                    .role("STUDENT")
                    .createdAt(LocalDateTime.now())
                    .build();

            User student2 = User.builder()
                    .username("student2")
                    .passwordHash(passwordEncoder.encode("student123"))
                    .email("student2@enicarthage.tn")
                    .role("STUDENT")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.saveAll(Arrays.asList(admin, student1, student2));

            // ========== STUDENTS ==========
            Student std1 = Student.builder()
                    .user(student1)
                    .firstName("Mohamed")
                    .lastName("Ben Ali")
                    .matricule("GI20220001")
                    .specialization(gi)
                    .entryYear(2022)
                    .build();

            Student std2 = Student.builder()
                    .user(student2)
                    .firstName("Ahmed")
                    .lastName("Ben Salah")
                    .matricule("GI20220002")
                    .specialization(gi)
                    .entryYear(2022)
                    .build();

            studentRepository.saveAll(Arrays.asList(std1, std2));

            // ========== SURVEY TEMPLATES ==========
            SurveyTemplate courseEvalTemplate = SurveyTemplate.builder()
                    .name("Évaluation des Modules")
                    .description("Template standard pour l'évaluation des modules")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            surveyTemplateRepository.save(courseEvalTemplate);

            // ========== SURVEY QUESTIONS ==========
            String[] likertOptions = {
                    "Pas du tout satisfait",
                    "Plutôt pas satisfait",
                    "Plutôt satisfait",
                    "Tout à fait satisfait"
            };
            String optionsString = String.join(",", likertOptions);

            SurveyQuestion q1 = SurveyQuestion.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .questionText("Les objectifs visés et les compétences ciblées pour ce module sont clairement annoncés dès le début")
                    .questionType("LIKERT")
                    .questionOrder(1)
                    .options(optionsString)
                    .build();

            SurveyQuestion q2 = SurveyQuestion.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .questionText("Le contenu du cours est en adéquation avec les objectifs et les compétences annoncés")
                    .questionType("LIKERT")
                    .questionOrder(2)
                    .options(optionsString)
                    .build();

            SurveyQuestion q3 = SurveyQuestion.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .questionText("Les supports utilisés (Diapos, polycopiés, ...) sont de bonnes qualités au niveau de la forme et du fond")
                    .questionType("LIKERT")
                    .questionOrder(3)
                    .options(optionsString)
                    .build();

            SurveyQuestion q4 = SurveyQuestion.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .questionText("Vos commentaires supplémentaires (optionnel)")
                    .questionType("FREE_TEXT")
                    .questionOrder(4)
                    .build();

            surveyQuestionRepository.saveAll(Arrays.asList(q1, q2, q3, q4));

            // ========== SURVEYS ==========
            Survey survey1 = Survey.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .semester(s1_2022)
                    .specialization(gi)
                    .yearOfStudy(1)
                    .title("Évaluation S1 2022/2023 - GI 1ère année")
                    .description("Enquête d'évaluation des modules du semestre 1")
                    .openDate(LocalDateTime.now().minusDays(1))
                    .closeDate(LocalDateTime.now().plusDays(7))
                    .isActive(true)
                    .build();

            Survey survey2 = Survey.builder()
                    .surveyTemplate(courseEvalTemplate)
                    .semester(s1_2023)
                    .specialization(gi)
                    .yearOfStudy(2)
                    .title("Évaluation S1 2023/2024 - GI 2ème année")
                    .description("Enquête d'évaluation des modules du semestre 1")
                    .openDate(LocalDateTime.now().plusDays(1))
                    .closeDate(LocalDateTime.now().plusDays(30))
                    .isActive(false)
                    .build();

            surveyRepository.saveAll(Arrays.asList(survey1, survey2));

            // ========== SAMPLE RESPONSES ==========
            SurveyResponse response1 = SurveyResponse.builder()
                    .survey(survey1)
                    .student(std1)
                    .submittedAt(LocalDateTime.now())
                    .isComplete(true)
                    .feedback("Très bon module dans l'ensemble")
                    .build();

            surveyResponseRepository.save(response1);

            // Course response for PPST
            CourseResponse cr1 = CourseResponse.builder()
                    .surveyResponse(response1)
                    .semesterCourse(gi1_ppst)
                    .order(1)
                    .build();

            // Answers for PPST
            QuestionResponse qr1 = QuestionResponse.builder()
                    .courseResponse(cr1)
                    .surveyQuestion(q1)
                    .responseValue("Tout à fait satisfait")
                    .responseTime(LocalDateTime.now())
                    .build();

            QuestionResponse qr2 = QuestionResponse.builder()
                    .courseResponse(cr1)
                    .surveyQuestion(q2)
                    .responseValue("Plutôt satisfait")
                    .responseTime(LocalDateTime.now())
                    .build();

            QuestionResponse qr3 = QuestionResponse.builder()
                    .courseResponse(cr1)
                    .surveyQuestion(q3)
                    .responseValue("Plutôt satisfait")
                    .responseTime(LocalDateTime.now())
                    .build();

            cr1.setQuestionResponses(Arrays.asList(qr1, qr2, qr3));
            response1.setCourseResponses(Arrays.asList(cr1));
            surveyResponseRepository.save(response1);
        }
    }
}