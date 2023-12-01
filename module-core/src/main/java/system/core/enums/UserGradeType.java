package system.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import system.core.exception.UserException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static system.core.enums.UserRoleType.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserGradeType {

    /**
     * 게스트 등급
     */
    GUEST(new UserRoleType[]{ R }),

    /**
     * 스케줄러 등급
     */
    SCHED(new UserRoleType[]{ R, W, X }),

    /**
     * 어드민 등급
     */
    SYSOP(new UserRoleType[]{ R, W, X, O });

    public static UserGradeType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow(() -> UserException.userGradeNotFound());
    }

    /**
     * 해당 등급이 특정 권한을 갖는지 확인한다.
     */
    public static Boolean hasRoleType(UserGradeType gradeType, UserRoleType targetType) {
        return Arrays.stream(gradeType.getRoleTypes())
                .anyMatch(type -> type == targetType);
    }

    /**
     * 해당 등급이 특정 권한들을 갖는지 확인한다.
     */
    public static Boolean hasRoleTypes(UserGradeType gradeType, UserRoleType... targetTypes) {
        return new HashSet<>(Arrays.asList(gradeType.getRoleTypes())).containsAll(Arrays.asList(targetTypes));
    }

    private final UserRoleType[] roleTypes;

    private static final Map<String, UserGradeType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(UserGradeType::name, Function.identity())));
}