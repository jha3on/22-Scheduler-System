package system.share.base.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.share.base.common.exception.CommonException;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SortType {
    /**
     * 내림차순 정렬 조건
     */
    DESC("내림차순"),

    /**
     * 오름차순 정렬 조건
     */
    ASC("오름차순");

    public static SortType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> CommonException.sortTypeNotFound());
    }

    private final String value;

    private static final Map<String, SortType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(SortType::name, Function.identity())));
}