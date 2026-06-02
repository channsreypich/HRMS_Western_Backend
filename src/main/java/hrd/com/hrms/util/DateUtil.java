package hrd.com.hrms.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private DateUtil() {} // Prevent instantiation

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // Convert String from frontend to LocalDate
    public static LocalDate parseToDate(String dateStr) {
        return (dateStr == null || dateStr.isBlank()) ? null : LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    // Convert LocalDate to String for frontend JSON responses
    public static String formatToString(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    // Convert LocalDateTime to String
    public static String formatToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATETIME_FORMATTER);
    }

    // Calculate days between two dates (useful for counting requested Leave days)
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end) + 1; // Inclusive of last day
    }
}