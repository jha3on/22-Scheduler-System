package system.core.enums.schedule;

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
public enum TriggerClassType {

    /**
     * Simple 트리거 유형
     */
    SP("SP, 반복 횟수, 반복 간격 기준"),

    /**
     * Cron 트리거 유형
     */
    CR("CR, 반복 표현식 기준");

    public static TriggerClassType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> TriggerException.unsupportedTriggerClassType());
    }

    private final String value;

    private static final Map<String, TriggerClassType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(TriggerClassType::name, Function.identity())));
}