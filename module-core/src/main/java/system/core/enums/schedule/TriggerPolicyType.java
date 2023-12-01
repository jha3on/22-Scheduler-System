package system.core.enums.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.core.exception.TriggerException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TriggerPolicyType {

    /**
     * Simple 트리거: 불발 무시
     */
    SP_IGNORE_MISFIRE("SP, 불발 무시"),

    /**
     * Simple 트리거: 즉시 재실행
     */
    SP_NOW_SCHEDULE("SP, 즉시 재실행"),

    /**
     * Simple 트리거: 즉시 재실행, 잔여 횟수 감소
     */
    SP_NOW_SCHEDULE_REDUCE_COUNT("SP, 즉시 재실행, 잔여 횟수 감소"),

    /**
     * Simple 트리거: 즉시 재실행, 잔여 횟수 유지
     */
    SP_NOW_SCHEDULE_RETAIN_COUNT("SP, 즉시 재실행, 잔여 횟수 유지"),

    /**
     * Simple 트리거: 다음 스케줄 실행, 잔여 횟수 감소
     */
    SP_NEXT_SCHEDULE_REDUCE_COUNT("SP, 다음 스케줄 실행, 잔여 횟수 감소"),

    /**
     * Simple 트리거: 다음 스케줄 실행, 잔여 횟수 유지
     */
    SP_NEXT_SCHEDULE_RETAIN_COUNT("SP, 다음 스케줄 실행, 잔여 횟수 유지"),

    /**
     * Cron 트리거: 불발 통과
     */
    CR_PASS_MISFIRE("CR, 불발 통과"),

    /**
     * Cron 트리거: 불발 무시
     */
    CR_IGNORE_MISFIRE("CR, 불발 무시"),

    /**
     * Cron 트리거: 즉시 재실행
     */
    CR_NOW_SCHEDULE("CR, 즉시 재실행");

    public static TriggerPolicyType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> TriggerException.unsupportedTriggerPolicyType());
    }

    public static Boolean isSimplePolicy(TriggerPolicyType type) {
        return spNames.containsKey(type.name());
    }

    public static Boolean isCronPolicy(TriggerPolicyType type) {
        return crNames.containsKey(type.name());
    }

    private final String value;

    private static final Map<String, TriggerPolicyType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(TriggerPolicyType::name, Function.identity())));

    private static final Map<String, TriggerPolicyType> spNames = Collections
            .unmodifiableMap(Arrays.stream(values()).filter(type -> type.name().contains("SP_"))
            .collect(Collectors.toMap(TriggerPolicyType::name, Function.identity())));

    private static final Map<String, TriggerPolicyType> crNames = Collections
            .unmodifiableMap(Arrays.stream(values()).filter(type -> type.name().contains("CR_"))
            .collect(Collectors.toMap(TriggerPolicyType::name, Function.identity())));
}