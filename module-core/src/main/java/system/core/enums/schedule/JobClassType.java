package system.core.enums.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.core.exception.JobException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JobClassType {

    /**
     * 내부 작업 클래스
     */
    LOCAL("내부 모듈"),

    /**
     * 외부 작업 클래스
     */
    REMOTE("외부 모듈");

    public static JobClassType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> JobException.jobClassTypeNotFound());
    }

    private final String value;

    private static final Map<String, JobClassType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(JobClassType::name, Function.identity())));
}