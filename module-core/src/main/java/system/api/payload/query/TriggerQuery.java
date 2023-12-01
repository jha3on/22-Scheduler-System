package system.api.payload.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.schedule.ScheduleStateType;
import system.core.enums.schedule.ScheduleStoreType;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import system.share.base.utility.SystemUtils;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerQuery {
    private Long jobSeq; // 작업 번호
    private Long triggerSeq; // 트리거 번호
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String triggerName; // 트리거 이름
    private String triggerGroup; // 트리거 그룹
    private String triggerRegister; // 트리거 등록자
    private String triggerClass; // 트리거 실행 유형
    private String triggerPolicy; // 트리거 정책 유형
    private String triggerState; // 트리거 상태 유형
    private String triggerStore; // 트리거 등록 유형
    private String repeatCount; // 반복 실행 횟수
    private String repeatInterval; // 반복 실행 간격
    private String repeatExpression; // 반복 실행 표현식
    private String prevFireTime; // 이전 실행 시간
    private String nextFireTime; // 다음 실행 시간
    private String startTime; // 시작 시간
    private String stopTime; // 종료 시간

    @QueryProjection
    public TriggerQuery(
        Long jobSeq,
        Long triggerSeq,
        String triggerName,
        String triggerGroup,
        String triggerRegister,
        ScheduleStateType triggerState,
        ScheduleStoreType triggerStore
    ) {
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.triggerRegister = triggerRegister;
        this.triggerState = triggerState.name();
        this.triggerStore = triggerStore.name();
    }

    @QueryProjection
    public TriggerQuery(
        Long jobSeq,
        Long triggerSeq,
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        String triggerRegister,
        ScheduleStateType triggerState,
        ScheduleStoreType triggerStore
    ) {
        this.jobSeq = jobSeq;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerSeq = triggerSeq;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.triggerRegister = triggerRegister;
        this.triggerState = triggerState.name();
        this.triggerStore = triggerStore.name();
    }

    @QueryProjection
    public TriggerQuery(
        Long jobSeq,
        Long triggerSeq,
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        String triggerRegister,
        TriggerClassType triggerClass,
        TriggerPolicyType triggerPolicy,
        ScheduleStateType triggerState,
        ScheduleStoreType triggerStore,
        Integer repeatCount,
        Integer repeatInterval,
        String repeatExpression,
        LocalDateTime prevFireTime,
        LocalDateTime nextFireTime,
        LocalDateTime startTime,
        LocalDateTime stopTime
    ) {
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.triggerRegister = triggerRegister;
        this.triggerClass = triggerClass.name();
        this.triggerPolicy = triggerPolicy.name();
        this.triggerState = triggerState.name();
        this.triggerStore = triggerStore.name();
        this.repeatCount = SystemUtils.empty(repeatCount);
        this.repeatInterval = SystemUtils.empty(repeatInterval);
        this.repeatExpression = SystemUtils.empty(repeatExpression);
        this.prevFireTime = SystemUtils.empty(prevFireTime);
        this.nextFireTime = SystemUtils.empty(nextFireTime);
        this.startTime = SystemUtils.empty(startTime);
        this.stopTime = SystemUtils.empty(stopTime);
    }
}