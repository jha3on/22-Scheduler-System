package system.share.base.converter;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import system.share.base.utility.DateTimeUtils;

@Slf4j
public class TypeDeserializer {
    public static LocalDateDeserializer localDate() {
        return new LocalDateDeserializer(DateTimeUtils.getDateFormatter());
    }

    public static LocalDateTimeDeserializer localDateTime() {
        return new LocalDateTimeDeserializer(DateTimeUtils.getDateTimeFormatter());
    }
}