package system.share.base.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationField {
    private String name;
    private String value;
    private String message;

    /**
     * 필드 유효성 객체를 생성한다.
     */
    public static ValidationField create(FieldError error) {
        String name = error.getField();
        String value = Objects.isNull(error.getRejectedValue()) ? "" : error.getRejectedValue().toString();
        String message = error.getDefaultMessage();

        return new ValidationField(name, value, message);
    }

    /**
     * 필드 유효성 객체를 생성한다.
     */
    public static ValidationField create(MethodArgumentTypeMismatchException error) {
        String name = error.getName();
        String value = Objects.isNull(error.getValue()) ? "" : error.getValue().toString();
        String message = error.getErrorCode();

        return new ValidationField(name, value, message);
    }
}