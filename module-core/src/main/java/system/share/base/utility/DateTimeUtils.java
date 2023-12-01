package system.share.base.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {
    public static final String FORMATTED_DATE_PATTERN = "yyyy-MM-dd";
    public static final String FORMATTED_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"; // HH: 00:00, kk: 24:00

    public static Integer getCurrentYear() {
        return LocalDate.now().getYear();
    }

    public static Integer getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(FORMATTED_DATE_PATTERN));
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMATTED_DATE_TIME_PATTERN));
    }

    public static LocalDateTime getMaxDateTime() {
        return LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(FORMATTED_DATE_PATTERN);
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(FORMATTED_DATE_TIME_PATTERN);
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String toString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(FORMATTED_DATE_TIME_PATTERN));
    }
}