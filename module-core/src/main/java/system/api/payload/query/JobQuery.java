package system.api.payload.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.schedule.JobClassType;
import system.core.enums.schedule.ScheduleStateType;
import system.core.enums.schedule.ScheduleStoreType;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobQuery {
    private Long jobSeq; // 작업 번호
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String jobRegister; // 작업 등록자
    private String jobClass; // 작업 대상 유형
    private String jobClassName; // 작업 대상 클래스
    private String jobClassData; // 작업 대상 데이터
    private String jobClassComment; // 작업 대상 설명
    private String jobState; // 작업 상태 유형
    private String jobStore; // 작업 등록 유형

    @QueryProjection
    public JobQuery(
        Long jobSeq,
        String jobName,
        String jobGroup,
        String jobRegister,
        ScheduleStateType jobState,
        ScheduleStoreType jobStore
    ) {
        this.jobSeq = jobSeq;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobRegister = jobRegister;
        this.jobState = jobState.name();
        this.jobStore = jobStore.name();
    }

    @QueryProjection
    public JobQuery(
        Long jobSeq,
        String jobName,
        String jobGroup,
        String jobRegister,
        JobClassType jobClass,
        String jobClassName,
        String jobClassData,
        String jobClassComment,
        ScheduleStateType jobState,
        ScheduleStoreType jobStore
    ) {
        this.jobSeq = jobSeq;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobRegister = jobRegister;
        this.jobClass = jobClass.name();
        this.jobClassName = jobClassName;
        this.jobClassData = jobClassData;
        this.jobClassComment = jobClassComment;
        this.jobState = jobState.name();
        this.jobStore = jobStore.name();
    }
}