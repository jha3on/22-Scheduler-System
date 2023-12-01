package system.core.entity.schedule;

import lombok.*;
import lombok.experimental.Accessors;
import system.api.payload.ScheduleLogSet;
import system.core.enums.schedule.ScheduleStateType;
import system.share.base.common.entity.AbstractLogEntity;
import javax.persistence.*;

@Getter
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"logSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_job_log")
public class JobLog extends AbstractLogEntity {

    @Id
    @Column(name = "log_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logSeq; // 로그 번호 (PK_1)

    @Column(name = "log_result", nullable = false)
    private String logResult; // 로그 내용

    @Column(name = "log_detail", nullable = false)
    private String logDetail; // 로그 상세 내용

    @Enumerated(value = EnumType.STRING)
    @Column(name = "log_state", nullable = false)
    private ScheduleStateType logState; // 로그 유형

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seq", nullable = false)
    private JobSummary jobSummary; // 작업 번호 (FK_1)

    @Builder(access = AccessLevel.PRIVATE)
    private JobLog(
        String logResult,
        String logDetail,
        ScheduleStateType logState,
        JobSummary jobSummary
    ) {
        this.logState = logState;
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.jobSummary = jobSummary;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JobLog create(
        String logResult,
        String logDetail,
        ScheduleStateType logState,
        JobSummary jobSummary
    ) {
        return JobLog.builder()
                .logState(logState)
                .logResult(logResult)
                .logDetail(logDetail)
                .jobSummary(jobSummary)
                .build();
    }

    public static JobLog create(JobSummary jobSummary, ScheduleLogSet logSet) {
        return JobLog.builder()
                .logState(logSet.getLogState())
                .logResult(logSet.getLogResult())
                .logDetail(logSet.getLogDetail())
                .jobSummary(jobSummary)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}