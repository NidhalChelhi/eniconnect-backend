package tn.enicarthage.eniconnect_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.enicarthage.eniconnect_backend.services.SurveySeederService;

@Configuration
public class DatabaseSeederConfig {

    @Bean
    public CommandLineRunner seedDatabase(SurveySeederService surveySeederService) {
        return args -> {
            // Uncomment to run the seeder
//            surveySeederService.seedAllPossibleSurveys();
        };
    }
}