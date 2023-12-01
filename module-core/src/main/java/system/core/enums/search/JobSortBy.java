package system.core.enums.search;

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
public enum JobSortBy {

    /**
     * 작업 번호 정렬 조건
     */
    SEQ("번호"),

    /**
     * 작업 이름 정렬 조건
     */
    NAME("이름"),

    /**
     * 작업 그룹 정렬 조건
     */
    GROUP("그룹"),

    /**
     * 작업 등록자 정렬 조건
     */
    REGISTER("등록자");

    public static JobSortBy getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> JobException.jobSortTypeNotFound());
    }

    private final String value;

    private static final Map<String, JobSortBy> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(JobSortBy::name, Function.identity())));
}