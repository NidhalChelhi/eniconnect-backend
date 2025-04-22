package tn.enicarthage.eniconnect_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.enums.*;
import tn.enicarthage.eniconnect_backend.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final UserAccountRepository userAccountRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;


    @Bean
    @Transactional
    CommandLineRunner seedDatabase() {
        return args -> {
            if (shouldSeed()) {
                log.info("⚡ Starting comprehensive database seeding...");
                seedUsersAndStudents();


                // 1. Seed Courses (20+ courses across all filières)
                List<Course> courses = seedCourses();

                // 2. Seed Questions (15+ questions of different types)
                List<Question> questions = seedQuestions();

                // 3. Create Multiple Surveys (5+ surveys)
                seedSurveys(courses, questions);


                log.info("✅ Database seeding completed with {} courses, {} questions, {} surveys, and {} students",
                        courseRepository.count(),
                        questionRepository.count(),
                        surveyRepository.count(),
                        studentRepository.count());
            } else {
                log.info("⏩ Database already contains data - skipping seeding");
            }
        };
    }


    private boolean shouldSeed() {
        return courseRepository.count() == 0 && questionRepository.count() == 0 && surveyRepository.count() == 0;
    }

    private void seedUsersAndStudents() {
        log.info("👤 Seeding initial users and students...");

        // Create Admin Student & Account
        Student adminStudent = new Student();
        adminStudent.setFirstName("Admin");
        adminStudent.setLastName("User");
        adminStudent.setEmail("admin@enicar.tn");
        adminStudent.setCin("10000000");
        adminStudent.setFiliere(Filiere.GI);
        adminStudent.setNiveau(Niveau.ING1);
        adminStudent.setClasse("GI-1");
        adminStudent.setSchoolYear("2023-2024");
        adminStudent.setVerified(true);
        adminStudent = studentRepository.save(adminStudent);

        UserAccount adminAccount = new UserAccount();
        adminAccount.setUsername("admin");
        adminAccount.setPassword(passwordEncoder.encode("Admin123!"));
        adminAccount.setRole(Role.ADMIN);
        adminAccount.setStudent(adminStudent);
        userAccountRepository.save(adminAccount);

        // Create Regular Student & Account
        Student testStudent = new Student();
        testStudent.setFirstName("Test");
        testStudent.setLastName("Student");
        testStudent.setEmail("student@enicar.tn");
        testStudent.setCin("20000000");
        testStudent.setFiliere(Filiere.GI);
        testStudent.setNiveau(Niveau.ING1);
        testStudent.setClasse("GI-1");
        testStudent.setSchoolYear("2023-2024");
        testStudent.setVerified(true);
        testStudent = studentRepository.save(testStudent);

        UserAccount studentAccount = new UserAccount();
        studentAccount.setUsername("student");
        studentAccount.setPassword(passwordEncoder.encode("Student123!"));
        studentAccount.setRole(Role.STUDENT);
        studentAccount.setStudent(testStudent);
        userAccountRepository.save(studentAccount);

        // Create unverified student (for signup testing)
        Student unverifiedStudent = new Student();
        unverifiedStudent.setFirstName("Unverified");
        unverifiedStudent.setLastName("Student");
        unverifiedStudent.setEmail("unverified@enicar.tn");
        unverifiedStudent.setCin("30000000");
        unverifiedStudent.setFiliere(Filiere.GINF);
        unverifiedStudent.setNiveau(Niveau.ING2);
        unverifiedStudent.setClasse("GINF-2");
        unverifiedStudent.setSchoolYear("2023-2024");
        unverifiedStudent.setVerified(false);
        unverifiedStudent.setAccountCreationToken(UUID.randomUUID().toString());
        studentRepository.save(unverifiedStudent);

        log.info("✅ Seeded 3 students (1 admin, 1 regular, 1 unverified)");
    }

    private List<Course> seedCourses() {
        List<Course> courses = List.of(
                // GI (Génie Informatique) Courses
                createCourse("INF101", "Programming Basics", Filiere.GI, Semester.S1), createCourse("INF102", "Data Structures", Filiere.GI, Semester.S1), createCourse("INF201", "Algorithms", Filiere.GI, Semester.S2), createCourse("INF202", "Database Systems", Filiere.GI, Semester.S2), createCourse("INF301", "Operating Systems", Filiere.GI, Semester.S3), createCourse("INF302", "Computer Networks", Filiere.GI, Semester.S3), createCourse("INF401", "Software Engineering", Filiere.GI, Semester.S4), createCourse("INF402", "Artificial Intelligence", Filiere.GI, Semester.S4),

                // GINF (Génie Informatique) Courses
                createCourse("NET101", "Network Fundamentals", Filiere.GINF, Semester.S1), createCourse("WEB201", "Web Development", Filiere.GINF, Semester.S2), createCourse("CLD301", "Cloud Computing", Filiere.GINF, Semester.S3), createCourse("SEC401", "Cybersecurity", Filiere.GINF, Semester.S4),

                // GM (Génie Mécanique) Courses
                createCourse("MEC101", "Mechanical Drawing", Filiere.GM, Semester.S1), createCourse("MEC202", "Thermodynamics", Filiere.GM, Semester.S2), createCourse("MEC303", "Fluid Mechanics", Filiere.GM, Semester.S3), createCourse("MEC404", "Automotive Systems", Filiere.GM, Semester.S4),

                // GIND (Génie Industriel) Courses
                createCourse("IND101", "Industrial Statistics", Filiere.GIND, Semester.S1), createCourse("IND202", "Operations Research", Filiere.GIND, Semester.S2), createCourse("IND303", "Quality Management", Filiere.GIND, Semester.S3), createCourse("IND404", "Supply Chain", Filiere.GIND, Semester.S4),

                // Common Courses
                createCourse("MAT101", "Mathematics I", Filiere.GI, Semester.S1), createCourse("PHY101", "Physics I", Filiere.GM, Semester.S1), createCourse("ENG101", "Technical English", Filiere.GIND, Semester.S1));

        return courseRepository.saveAll(courses);
    }

    private Course createCourse(String code, String name, Filiere filiere, Semester semester) {
        Course course = new Course();
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setFiliere(filiere);
        course.setSemester(semester);
        return course;
    }

    private List<Question> seedQuestions() {
        List<Question> questions = List.of(
                // Likert Scale Questions (4 options)
                createLikertQuestion("Les objectifs du module sont clairement définis"), createLikertQuestion("Le contenu du cours correspond aux objectifs"), createLikertQuestion("La charge de travail est adaptée"), createLikertQuestion("Les supports de cours sont de bonne qualité"), createLikertQuestion("L'enseignant est compétent dans sa matière"), createLikertQuestion("Les méthodes pédagogiques sont efficaces"), createLikertQuestion("Les évaluations reflètent bien les acquis"), createLikertQuestion("Les ressources pédagogiques sont suffisantes"),

                // Likert Scale Questions (5 options)
                createLikert5Question("La difficulté du cours est bien adaptée"), createLikert5Question("Le rythme des séances est approprié"),

                // Text Questions
                createTextQuestion("Commentaires libres sur le module"), createTextQuestion("Suggestions d'amélioration"), createTextQuestion("Points forts du module"), createTextQuestion("Points faibles du module"), createTextQuestion("Autres remarques"));

        return questionRepository.saveAll(questions);
    }

    private Question createLikertQuestion(String text) {
        Question question = new Question();
        question.setText(text);
        question.setType(QuestionType.LIKERT_4);
        question.setOptions(List.of("Pas du tout satisfait", "Plutôt pas satisfait", "Plutôt satisfait", "Tout à fait satisfait"));
        return question;
    }

    private Question createLikert5Question(String text) {
        Question question = new Question();
        question.setText(text);
        question.setType(QuestionType.LIKERT_5);
        question.setOptions(List.of("Très en désaccord", "En désaccord", "Neutre", "D'accord", "Tout à fait d'accord"));
        return question;
    }

    private Question createTextQuestion(String text) {
        Question question = new Question();
        question.setText(text);
        question.setType(QuestionType.TEXT);
        return question;
    }

    private void seedSurveys(List<Course> courses, List<Question> questions) {
        // Current semester survey
        Survey currentEval = createSurvey("Survey S1 2023 - Génie Informatique", Filiere.GI, Semester.S1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(14), courses, questions.subList(0, 8) // First 8 questions
        );

        // Past survey
        Survey pastEval = createSurvey("Survey S2 2022 - Génie Mécanique", Filiere.GM, Semester.S2, LocalDateTime.now().minusMonths(6), LocalDateTime.now().minusMonths(5), courses, questions.subList(4, 12) // Middle questions
        );

        // Future survey
        Survey futureEval = createSurvey("Survey S3 2024 - Génie Industriel", Filiere.GIND, Semester.S3, LocalDateTime.now().plusMonths(3), LocalDateTime.now().plusMonths(4), courses, questions.subList(8, 15) // Last questions
        );

        // Additional surveys
        createSurvey("Survey S4 2023 - Génie Informatique", Filiere.GI, Semester.S4, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(5), courses, questions);

        createSurvey("Survey S1 2023 - Génie Mécanique", Filiere.GM, Semester.S1, LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(5), courses, questions.subList(0, 5));
    }

    private Survey createSurvey(String title, Filiere filiere, Semester semester, LocalDateTime openDate, LocalDateTime closeDate, List<Course> allCourses, List<Question> questions) {
        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setFiliere(filiere);
        survey.setSemester(semester);
        survey.setOpenDate(openDate);
        survey.setCloseDate(closeDate);

        // Filter courses by filiere and semester
        survey.setCourses(allCourses.stream().filter(c -> c.getFiliere() == filiere && c.getSemester() == semester).toList());

        survey = surveyRepository.save(survey);

        // Add questions with proper ordering
        for (int i = 0; i < questions.size(); i++) {
            SurveyQuestion eq = new SurveyQuestion();
            eq.setSurvey(survey);
            eq.setQuestion(questions.get(i));
            eq.setDisplayOrder(i + 1);
            surveyQuestionRepository.save(eq);
        }

        log.info("📝 Created {} survey with {} courses and {} questions", survey.getTitle(), survey.getCourses().size(), questions.size());

        return survey;
    }


}
