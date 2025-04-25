package tn.enicarthage.eniconnect_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.utils.AcademicUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initializeTestData();
        }
    }

    private void initializeTestData() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = LocalDate.now().getYear();

        // Create specializations
        Specialization gi = createSpecialization("Génie Informatique", "GI");
        Specialization info = createSpecialization("Infotronique", "INFO");
        Specialization mct = createSpecialization("Mécatronique", "MCT");
        Specialization gind = createSpecialization("Génie Industriel", "GIND");

        // Create academic years for past 4 years and current year
        for (int year = currentYear - 3; year <= currentYear; year++) {
            createAcademicYear(year + "/" + (year + 1), year, year + 1);
        }

        // Get all academic years
        List<AcademicYear> academicYears = academicYearRepository.findAll();

        // Create semesters for each academic year
        for (AcademicYear year : academicYears) {
            createSemester(year, 1,
                    LocalDateTime.of(year.getStartYear(), 9, 15, 0, 0),
                    LocalDateTime.of(year.getEndYear(), 1, 31, 0, 0));
            createSemester(year, 2,
                    LocalDateTime.of(year.getEndYear(), 2, 15, 0, 0),
                    LocalDateTime.of(year.getEndYear(), 6, 30, 0, 0));
        }


        //  Create Courses for all specializations
        Course math1 = createCourse("Mathématiques 1", "MATH1", "Cours de mathématiques de base");
        Course phys1 = createCourse("Physique 1", "PHYS1", "Cours de physique fondamentale");
        Course info1 = createCourse("Informatique 1", "INFO1", "Introduction à la programmation");
        Course anglais = createCourse("Anglais Technique", "ANG1", "Anglais pour ingénieurs");
        Course comm = createCourse("Communication", "COMM1", "Techniques de communication");

        // GI-specific courses
        Course algo = createCourse("Algorithmique avancée", "ALGO", "Structures de données et algorithmes");
        Course bd = createCourse("Bases de données", "BD", "Modélisation et implémentation de bases de données");
        Course web = createCourse("Développement Web", "WEB", "Technologies web modernes");
        Course sys = createCourse("Systèmes d'exploitation", "SYSEXP", "Fonctionnement des systèmes d'exploitation");
        Course reseaux = createCourse("Réseaux informatiques", "RESEAUX", "Principes des réseaux informatiques");
        Course ia = createCourse("Introduction à l'IA", "IA1", "Bases de l'intelligence artificielle");

        // INFO-specific courses
        Course elec = createCourse("Électronique", "ELEC", "Circuits électroniques");
        Course telecom = createCourse("Télécommunications", "TCOM", "Principes des télécoms");
        Course micro = createCourse("Microcontrôleurs", "MICRO", "Programmation des microcontrôleurs");
        Course capteurs = createCourse("Capteurs", "CAPT", "Technologie des capteurs");

        // MCT-specific courses
        Course meca = createCourse("Mécanique", "MECA", "Mécanique des solides");
        Course robot = createCourse("Robotique", "ROBOT", "Principes de robotique");
        Course auto = createCourse("Automatique", "AUTO", "Systèmes automatisés");
        Course cfd = createCourse("CFD", "CFD", "Dynamique des fluides numérique");

        // GIND-specific courses
        Course gestion = createCourse("Gestion industrielle", "GEST", "Gestion de production");
        Course qualite = createCourse("Qualité", "QUAL", "Management de la qualité");
        Course log = createCourse("Logistique", "LOG", "Gestion de la logistique");
        Course ergo = createCourse("Ergonomie", "ERGO", "Ergonomie industrielle");

        // Create semester courses for each specialization and year
        List<Semester> semesters = semesterRepository.findAll();
        for (Semester semester : semesters) {
            int yearOfStudy = calculateYearOfStudy(semester);

            // Create courses for each specialization
            for (Specialization spec : List.of(gi, info, mct, gind)) {
                List<Course> courses = getCoursesForSpecializationAndYear(spec, yearOfStudy);
                createSemesterCourses(semester, spec, yearOfStudy, courses);
            }
        }

        // Create admin user
        User admin = createUser("admin", "admin123", "admin@enicarthage.tn", "ADMIN");


        // GI students
// Current year students
        createStudent("Mohamed", "Ben Ali", "mbenali", "student123",
                "mbenali@enicarthage.tn", gi, currentYear - 0); // 1st year
        createStudent("Leila", "Ben Ahmed", "lbenahmed", "student123",
                "lbenahmed@enicarthage.tn", gi, currentYear - 1); // 2nd year
        createStudent("Ahmed", "Ben Salah", "abensalah", "student123",
                "abensalah@enicarthage.tn", gi, currentYear - 2); // 3rd year

        SurveyTemplate template = createSurveyTemplate("Évaluation des Modules",
                "Template standard pour l'évaluation des modules", true);

        // 9. Add Questions (in French) with Likert scale options
        String[] options = {"Pas du tout satisfait", "Plutôt pas satisfait", "Plutôt satisfait", "Tout à fait satisfait"};

        createSurveyQuestion(template, "Les objectifs du module sont clairs", "LIKERT", 1, options);
        createSurveyQuestion(template, "Le contenu correspond aux objectifs", "LIKERT", 2, options);
        createSurveyQuestion(template, "La charge de travail est appropriée", "LIKERT", 3, options);
        createSurveyQuestion(template, "Les supports de cours sont de qualité", "LIKERT", 4, options);
        createSurveyQuestion(template, "L'enseignant est compétent dans son domaine", "LIKERT", 5, options);
        createSurveyQuestion(template, "L'enseignant est disponible pour les étudiants", "LIKERT", 6, options);
        createSurveyQuestion(template, "Les évaluations reflètent bien les acquis", "LIKERT", 7, options);
        createSurveyQuestion(template, "Commentaires généraux sur le module", "FREE_TEXT", 8, null);

// Create surveys for each year of study
        AcademicYear currentAcademicYear = academicYearRepository.findByStartYearAndEndYear(currentYear, currentYear + 1);
        Semester currentSemester = semesterRepository.findByAcademicYearAndNumber(currentAcademicYear, 2);

        for (int yearOfStudy = 1; yearOfStudy <= 3; yearOfStudy++) {
            createSurvey(template, currentSemester, gi, yearOfStudy,
                    "Évaluation S2 " + currentYear + "/" + (currentYear + 1) + " - GI " + yearOfStudy + "ème année",
                    "Enquête d'évaluation des modules du semestre 2",
                    now.minusDays(1), now.plusDays(14), true);
        }


    }

    private int calculateYearOfStudy(Semester semester) {
        // Calculate based on academic year sequence
        // First year: semesters 1 and 2
        // Second year: semesters 3 and 4
        // Third year: semester 5
        return (semester.getNumber() + 1) / 2;
    }

    private List<Course> getCoursesForSpecializationAndYear(Specialization spec, int yearOfStudy) {
        // Implement logic to return appropriate courses for each spec and year
        // This is simplified - you should implement your actual curriculum
        if (yearOfStudy == 1) {
            return courseRepository.findByCodeIn(List.of("MATH1", "PHYS1", "INFO1", "ANG1", "COMM1"));
        } else if (spec.getCode().equals("GI") && yearOfStudy == 2) {
            return courseRepository.findByCodeIn(List.of("ALGO", "BD", "WEB", "SYSEXP"));
        }
        // Add more cases as needed
        return List.of();
    }

    private Specialization createSpecialization(String name, String code) {
        return specializationRepository.save(
                Specialization.builder()
                        .name(name)
                        .code(code)
                        .build());
    }

    private AcademicYear createAcademicYear(String name, int startYear, int endYear) {
        return academicYearRepository.save(
                AcademicYear.builder()
                        .name(name)
                        .startYear(startYear)
                        .endYear(endYear)
                        .build());
    }

    private Semester createSemester(AcademicYear academicYear, int number,
                                    LocalDateTime startDate, LocalDateTime endDate) {
        return semesterRepository.save(
                Semester.builder()
                        .academicYear(academicYear)
                        .number(number)
                        .startDate(startDate)
                        .endDate(endDate)
                        .build());
    }

    private Course createCourse(String name, String code, String description) {
        return courseRepository.save(
                Course.builder()
                        .name(name)
                        .code(code)
                        .description(description)
                        .build());
    }

    private void createSemesterCourses(Semester semester, Specialization specialization,
                                       int yearOfStudy, List<Course> courses) {
        courses.forEach(course -> {
            semesterCourseRepository.save(
                    SemesterCourse.builder()
                            .semester(semester)
                            .specialization(specialization)
                            .course(course)
                            .yearOfStudy(yearOfStudy)
                            .build());
        });
    }

    private User createUser(String username, String password, String email, String role) {
        return userRepository.save(
                User.builder()
                        .username(username)
                        .passwordHash(passwordEncoder.encode(password))
                        .email(email)
                        .role(role)
                        .build());
    }

    private Student createStudent(String firstName, String lastName, String username,
                                  String password, String email, Specialization specialization,
                                  int entryYear) {
        User user = createUser(username, password, email, "STUDENT");
        return studentRepository.save(
                Student.builder()
                        .user(user)
                        .firstName(firstName)
                        .lastName(lastName)
                        .matricule(AcademicUtils.generateMatricule(
                                specialization.getCode(),
                                studentRepository.countBySpecializationCode(specialization.getCode())))
                        .specialization(specialization)
                        .entryYear(entryYear)
                        .build());
    }

    private SurveyTemplate createSurveyTemplate(String name, String description, boolean isSystemDefault) {
        return surveyTemplateRepository.save(
                SurveyTemplate.builder()
                        .name(name)
                        .description(description)
                        .isSystemDefault(isSystemDefault)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
    }

    private SurveyQuestion createSurveyQuestion(SurveyTemplate template, String questionText,
                                                String questionType, int questionOrder, String[] options) {
        return surveyQuestionRepository.save(
                SurveyQuestion.builder()
                        .surveyTemplate(template)
                        .questionText(questionText)
                        .questionType(questionType)
                        .questionOrder(questionOrder)
                        .options(options != null ? String.join(",", options) : null)
                        .build());
    }

    private Survey createSurvey(SurveyTemplate template, Semester semester, Specialization specialization,
                                int yearOfStudy, String title, String description,
                                LocalDateTime openDate, LocalDateTime closeDate, boolean isActive) {
        return surveyRepository.save(
                Survey.builder()
                        .surveyTemplate(template)
                        .semester(semester)
                        .specialization(specialization)
                        .yearOfStudy(yearOfStudy)
                        .title(title)
                        .description(description)
                        .openDate(openDate)
                        .closeDate(closeDate)
                        .isActive(isActive)
                        .build());
    }
}