package system.core.entity;

import lombok.*;
import lombok.experimental.Accessors;
import system.api.payload.UserLogSet;
import system.core.entity.schedule.JobSummary;
import system.core.entity.schedule.TriggerSummary;
import system.core.enums.UserLogType;
import system.share.base.common.entity.AbstractLogEntity;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"logSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user_log")
public class UserLog extends AbstractLogEntity {

    @Id
    @Column(name = "log_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logSeq; // 로그 번호 (PK_1)

    @Column(name = "log_result", nullable = false)
    private String logResult; // 로그 내용

    @Column(name = "log_detail", nullable = false)
    private String logDetail; // 로그 상세 내용

    @Enumerated(value = EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private UserLogType logType; // 로그 유형

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user; // 사용자 번호 (FK_1)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seq")
    private JobSummary jobSummary; // 작업 요약 (FK_2)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_seq")
    private TriggerSummary triggerSummary; // 트리거 요약 (FK_3)

    @Builder(access = AccessLevel.PRIVATE)
    private UserLog(
        User user,
        String logResult,
        String logDetail,
        UserLogType logType,
        JobSummary jobSummary,
        TriggerSummary triggerSummary
    ) {
        this.user = user;
        this.logType = logType;
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.jobSummary = jobSummary;
        this.triggerSummary = triggerSummary;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static UserLog create(User user, JobSummary jSummary, UserLogSet logSet) {
        return UserLog.builder()
                .user(user)
                .jobSummary(jSummary)
                .logType(logSet.getLogType())
                .logResult(logSet.getLogResult())
                .logDetail(logSet.getLogDetail())
                .build();
    }

    public static UserLog create(User user, TriggerSummary tSummary, UserLogSet logSet) {
        return UserLog.builder()
                .user(user)
                .triggerSummary(tSummary)
                .logType(logSet.getLogType())
                .logResult(logSet.getLogResult())
                .logDetail(logSet.getLogDetail())
                .build();
    }

    public static UserLog create(User user, JobSummary jSummary, TriggerSummary tSummary, UserLogSet logSet) {
        return UserLog.builder()
                .user(user)
                .jobSummary(jSummary)
                .triggerSummary(tSummary)
                .logType(logSet.getLogType())
                .logResult(logSet.getLogResult())
                .logDetail(logSet.getLogDetail())
                .build();
    }

    public static List<UserLog> create(User user, JobSummary jSummary, List<TriggerSummary> tSummaries, UserLogSet logSet) {
        return tSummaries.stream()
                .map(tSummary -> create(user, jSummary, tSummary, logSet))
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------
}