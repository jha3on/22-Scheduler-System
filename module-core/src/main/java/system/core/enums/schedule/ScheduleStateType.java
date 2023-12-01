package system.core.enums.schedule;

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
public enum ScheduleStateType {

    /**
     * 스케줄 생성
     */
    STORED,

    /**
     * 스케줄 수정
     */
    UPDATED,

    /**
     * 스케줄 재실행
     */
    RESUMED,

    /**
     * 스케줄 삭제
     */
    DELETED,

    /**
     * 스케줄 실행 준비
     * <p> : Trigger.TriggerState.NORMAL
     */
    SCHEDULED,

    /**
     * 스케줄 실행
     * <p> : Trigger.TriggerState.BLOCKED
     */
    EXECUTED,

    /**
     * 스케줄 정지
     * <p> : Trigger.TriggerState.PAUSED
     */
    PAUSED,

    /**
     * 스케줄 실행 기각
     * <p> : TriggerListener.vetoJobExecution(), JobListener.jobExecutionVetoed()
     */
    REJECTED,

    /**
     * 스케줄 실행 완료
     * <p> : Trigger.TriggerState.COMPLETE
     */
    COMPLETED,

    /**
     * 스케줄 실행 오류
     * <p> : Trigger.TriggerState.ERROR
     */
    ERRORED,

    /**
     * 스케줄 실행 종료
     * <p> : Trigger.TriggerState.COMPLETE, TriggerListener.getNextFireTime(null)
     */
    TERMINATED,

    /**
     * 스케줄 없음
     * <p> : Trigger.TriggerState.NONE
     */
    NONE,

    /**
     * 작업 처리 성공
     */
    SUCCESS,

    /**
     * 작업 처리 중
     */
    PROCESSING,

    /**
     * 작업 처리 실패
     */
    FAILURE;

    public static ScheduleStateType getByName(String name) {
        return Optional.ofNullable(names.get(name))
                .orElseThrow();
    }

    private static final Map<String, ScheduleStateType> names = Collections
            .unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(ScheduleStateType::name, Function.identity())));
}