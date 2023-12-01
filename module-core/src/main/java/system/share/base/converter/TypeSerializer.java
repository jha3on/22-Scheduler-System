package system.share.base.converter;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import system.share.base.utility.DateTimeUtils;

@Slf4j
public class TypeSerializer {
    public static LocalDateSerializer localDate() {
        return new LocalDateSerializer(DateTimeUtils.getDateFormatter());
    }

    public static LocalDateTimeSerializer localDateTime() {
        return new LocalDateTimeSerializer(DateTimeUtils.getDateTimeFormatter());
    }
}