package system.core.enums.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.core.exception.TriggerException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TriggerSortBy {

    /**
     * 트리거 번호 정렬 조건
     */
    SEQ("번호"),

    /**
     * 트리거 이름 정렬 조건
     */
    NAME("이름"),

    /**
     * 트리거 그룹 정렬 조건
     */
    GROUP("그룹"),

    /**
     * 트리거 등록자 정렬 조건
     */
    REGISTER("등록자");

    public static TriggerSortBy getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> TriggerException.triggerSortTypeNotFound());
    }

    private final String value;

    private static final Map<String, TriggerSortBy> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(TriggerSortBy::name, Function.identity())));
}