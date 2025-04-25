package tn.enicarthage.eniconnect_backend.utils;

import java.time.LocalDate;

public class AcademicUtils {

    public static int calculateYearOfStudy(int entryYear) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int month = now.getMonthValue();

        // Academic year starts in September
        if (month < 9) {
            currentYear--;
        }

        int yearOfStudy = currentYear - entryYear + 1;
        return Math.max(1, Math.min(3, yearOfStudy)); // Cap between 1-3
    }

    public static String generateMatricule(String specializationCode, long studentCount) {
        int currentYear = LocalDate.now().getYear() % 100;
        return String.format("%s%02d%04d", specializationCode, currentYear, studentCount + 1);
    }
}