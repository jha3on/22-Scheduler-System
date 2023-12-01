package system.api.payload.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.schedule.ScheduleStateType;
import system.share.base.utility.DateTimeUtils;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerLogQuery {
    private Long logSeq; // 로그 번호
    private Long jobSeq; // 작업 번호
    private Long triggerSeq; // 트리거 번호
    private String logResult; // 로그 내용
    private String logDetail; // 로그 상세 내용
    private String logState; // 로그 상태
    private String logTime; // 로그 시간

    @QueryProjection
    public TriggerLogQuery(
        Long logSeq,
        Long jobSeq,
        Long triggerSeq,
        String logResult,
        ScheduleStateType logState,
        LocalDateTime logTime
    ) {
        this.logSeq = logSeq;
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.logResult = logResult;
        this.logState = logState.name();
        this.logTime = DateTimeUtils.toString(logTime);
    }

    @QueryProjection
    public TriggerLogQuery(
        Long logSeq,
        Long jobSeq,
        Long triggerSeq,
        String logResult,
        String logDetail,
        ScheduleStateType logState,
        LocalDateTime logTime
    ) {
        this.logSeq = logSeq;
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.logState = logState.name();
        this.logTime = DateTimeUtils.toString(logTime);
    }
}