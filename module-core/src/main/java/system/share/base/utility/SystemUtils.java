package system.share.base.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemUtils {
    public static String unique() {
        return RandomStringUtils
                .randomAlphanumeric(10)
                .toLowerCase();
    }

    /**
     * -1 값을 확인한다.
     * <p> : TriggerQueryRepository.getTriggerSummary()
     */
    public static String empty(Integer value) {
        if (value == -1) return "";

        return String.valueOf(value);
    }

    /**
     * -1 값을 확인한다.
     * <p> : TriggerQueryRepository.getTriggerSummary()
     */
    public static String empty(String value) {
        if (value.equals("-1")) return "";

        return value;
    }

    /**
     * 날짜를 문자열로 변환한다.
     * <p> : TriggerQueryRepository.getTriggerSummary()
     */
    public static String empty(LocalDateTime value) {
        if (Objects.equals(DateTimeUtils.getMaxDateTime(), value)) return "";

        return DateTimeUtils.toString(value);
    }

    /**
     * 문자열을 변환한다.
     * <p> : string -> String, STRING -> String
     */
    public static String capitalize(String value) {
        return StringUtils.capitalize(value.toLowerCase());
    }
}