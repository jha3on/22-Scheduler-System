package system.api.payload;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.UserLogType;
import system.share.base.utility.SystemUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLogSet {
    private String logResult;
    private String logDetail;
    private UserLogType logType;

    @Builder(access = AccessLevel.PRIVATE)
    private UserLogSet(String logResult, String logDetail, UserLogType logType) {
        this.logType = logType;
        this.logResult = logResult;
        this.logDetail = logDetail;
    }

    /**
     * 작업 액션 로그를 생성한다.
     */
    public static UserLogSet job(UserLogType logType) {
        String logResult = String.format("[LOG] Job %s", SystemUtils.capitalize(logType.name()));
        String logDetail = "";

        return new UserLogSet(logResult, logDetail, logType);
    }

    /**
     * 작업 액션 로그를 생성한다.
     */
    public static UserLogSet job(UserLogType logType, String logDetail) {
        String logResult = String.format("[LOG] Job %s", SystemUtils.capitalize(logType.name()));

        return new UserLogSet(logResult, logDetail, logType);
    }

    /**
     * 트리거 액션 로그를 생성한다.
     */
    public static UserLogSet trigger(UserLogType logType) {
        String logResult = String.format("[LOG] Trigger %s", SystemUtils.capitalize(logType.name()));
        String logDetail = "";

        return new UserLogSet(logResult, logDetail, logType);
    }

    /**
     * 트리거 액션 로그를 생성한다.
     */
    public static UserLogSet trigger(UserLogType logType, String logDetail) {
        String logResult = String.format("[LOG] Trigger %s", SystemUtils.capitalize(logType.name()));

        return new UserLogSet(logResult, logDetail, logType);
    }
}