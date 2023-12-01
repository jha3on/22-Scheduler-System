package system.core.enums;

import system.core.exception.UserException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRoleType {

    /**
     * 스케줄 조회 권한
     */
    R("스케줄 조회"),

    /**
     * 스케줄 관리 권한
     */
    W("스케줄 관리"),

    /**
     * 스케줄 실행 권한
     */
    X("스케줄 실행"),

    /**
     * 시스템 운영 권한
     */
    O("시스템 운영");

    public static UserRoleType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> UserException.userRoleNotFound());
    }

    private final String value;

    private static final Map<String, UserRoleType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(UserRoleType::name, Function.identity())));
}