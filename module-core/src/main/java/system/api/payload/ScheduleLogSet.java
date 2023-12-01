package system.api.payload;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.schedule.ScheduleStateType;
import system.share.base.utility.SystemUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleLogSet {
    private String logResult; // 로그 내용
    private String logDetail; // 로그 상세 내용
    private ScheduleStateType logState; // 로그 유형

    @Builder(access = AccessLevel.PRIVATE)
    private ScheduleLogSet(String logResult, String logDetail, ScheduleStateType logState) {
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.logState = logState;
    }

    /**
     * 작업 액션 로그를 생성한다.
     */
    public static ScheduleLogSet job(ScheduleStateType logState) {
        String logResult = String.format("[LOG] Job %s", SystemUtils.capitalize(logState.name()));
        String logDetail = "";

        return new ScheduleLogSet(logResult, logDetail, logState);
    }

    /**
     * 작업 액션 로그를 생성한다.
     */
    public static ScheduleLogSet job(ScheduleStateType logState, String logDetail) {
        String logResult = String.format("[LOG] Job %s", SystemUtils.capitalize(logState.name()));

        return new ScheduleLogSet(logResult, logDetail, logState);
    }

    /**
     * 트리거 액션 로그를 생성한다.
     */
    public static ScheduleLogSet trigger(ScheduleStateType logState) {
        String logResult = String.format("[LOG] Trigger %s", SystemUtils.capitalize(logState.name()));
        String logDetail = "";

        return new ScheduleLogSet(logResult, logDetail, logState);
    }

    /**
     * 트리거 액션 로그를 생성한다.
     */
    public static ScheduleLogSet trigger(ScheduleStateType logState, String logDetail) {
        String logResult = String.format("[LOG] Trigger %s", SystemUtils.capitalize(logState.name()));

        return new ScheduleLogSet(logResult, logDetail, logState);
    }
}