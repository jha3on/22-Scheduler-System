package system.core.enums.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.core.exception.UserException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSortBy {

    /**
     * 사용자 번호 정렬 조건
     */
    SEQ("번호"),

    /**
     * 사용자 사번 정렬 조건
     */
    CODE("사번"),

    /**
     * 사용자 이름 정렬 조건
     */
    NAME("이름");

    public static UserSortBy getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> UserException.userSortTypeNotFound());
    }

    private final String value;

    private static final Map<String, UserSortBy> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(UserSortBy::name, Function.identity())));
}