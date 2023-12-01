package system.core.entity.schedule;

import lombok.*;
import lombok.experimental.Accessors;
import system.api.payload.ScheduleLogSet;
import system.core.enums.schedule.ScheduleStateType;
import system.share.base.common.entity.AbstractLogEntity;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"logSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_trigger_log")
public class TriggerLog extends AbstractLogEntity {

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
    @JoinColumn(name = "trigger_seq", nullable = false)
    private TriggerSummary triggerSummary; // 트리거 번호 (FK_1)

    @Builder(access = AccessLevel.PRIVATE)
    private TriggerLog(
        String logResult,
        String logDetail,
        ScheduleStateType logState,
        TriggerSummary triggerSummary
    ) {
        this.logState = logState;
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.triggerSummary = triggerSummary;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static TriggerLog create(
        String logResult,
        String logDetail,
        ScheduleStateType logState,
        TriggerSummary triggerSummary
    ) {
        return TriggerLog.builder()
                .logState(logState)
                .logResult(logResult)
                .logDetail(logDetail)
                .triggerSummary(triggerSummary)
                .build();
    }

    public static TriggerLog create(TriggerSummary triggerSummary, ScheduleLogSet logSet) {
        return TriggerLog.builder()
                .logState(logSet.getLogState())
                .logResult(logSet.getLogResult())
                .logDetail(logSet.getLogDetail())
                .triggerSummary(triggerSummary)
                .build();
    }

    public static List<TriggerLog> create(List<TriggerSummary> triggerSummaries, ScheduleLogSet logSet) {
        return triggerSummaries.stream()
                .map(triggerSummary -> create(triggerSummary, logSet))
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------
}