package system.share.base.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.Reader;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String serialize(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T deserialize(Reader src, Class<T> type) throws IOException {
        return mapper.readValue(src, type);
    }

    public static <T> T deserialize(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }

    public static <T> T convert(Object object, Class<T> type) {
        return mapper.convertValue(object, type);
    }
}