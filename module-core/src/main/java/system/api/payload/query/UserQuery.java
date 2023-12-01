package system.api.payload.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.UserGradeType;
import system.core.enums.UserStoreType;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQuery {
    private Long userSeq; // 사용자 번호
    private String userCode; // 사용자 사번
    private String userName; // 사용자 이름
    private String userEmail; // 사용자 이메일
    private String userGrade; // 사용자 등급 유형
    private String userStore; // 사용자 등록 유형

    @QueryProjection
    public UserQuery(
        Long userSeq,
        String userCode,
        String userName,
        String userEmail,
        UserGradeType userGrade,
        UserStoreType userStore
    ) {
        this.userSeq = userSeq;
        this.userCode = userCode;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userGrade = userGrade.name();
        this.userStore = userStore.name();
    }
}