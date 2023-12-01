package system.share.base.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.Errors;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationResult {
    private List<ValidationField> errors;

    /**
     * 유효성 확인 객체를 생성한다.
     */
    public static ValidationResult create(Errors errors) {
        List<ValidationField> fields = errors.getFieldErrors()
                .stream()
                .map(error -> ValidationField.create(error))
                .collect(Collectors.toList());

        return new ValidationResult(fields);
    }

    /**
     * 유효성 확인 객체를 생성한다.
     */
    public static ValidationResult create(MethodArgumentTypeMismatchException errors) {
        List<ValidationField> fields = new ArrayList<>();
        fields.add(ValidationField.create(errors));

        return new ValidationResult(fields);
    }
}