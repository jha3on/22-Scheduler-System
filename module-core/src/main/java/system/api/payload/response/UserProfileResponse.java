package system.api.payload.response;

import lombok.*;
import system.core.entity.User;
import system.core.enums.UserGradeType;
import system.core.enums.UserRoleType;
import system.core.enums.UserStoreType;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponse {
    private Long userSeq; // 사용자 번호
    private String userCode; // 사용자 코드
    private String userName; // 사용자 이름
    private String userEmail; // 사용자 이메일
    private UserGradeType userGrade; // 사용자 등급 유형
    private UserStoreType userStore; // 사용자 등록 유형
    private List<UserRoleType> userRoles; // 사용자 권한 목록

    public static UserProfileResponse create(User entity) {
        return UserProfileResponse.builder()
                .userSeq(entity.getUserSeq())
                .userCode(entity.getUserCode())
                .userName(entity.getUserName())
                .userEmail(entity.getUserEmail())
                .userGrade(entity.getGradeType())
                .userStore(entity.getStoreType())
                .userRoles(List.of(entity.getGradeType().getRoleTypes()))
                .build();
    }
}