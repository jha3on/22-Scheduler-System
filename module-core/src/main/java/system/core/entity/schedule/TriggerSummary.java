package system.core.entity.schedule;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import system.core.enums.schedule.ScheduleStateType;
import system.core.enums.schedule.ScheduleStoreType;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import system.core.exception.TriggerException;
import system.share.base.common.entity.AbstractEntity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@DynamicUpdate
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"triggerSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_trigger_summary", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"trigger_name", "trigger_group"}),
})
public class TriggerSummary extends AbstractEntity {

    @Id
    @Column(name = "trigger_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerSeq; // 트리거 번호 (PK_1)

    @Column(name = "job_name", nullable = false)
    private String jobName; // 작업 이름

    @Column(name = "job_group", nullable = false)
    private String jobGroup; // 작업 그룹

    @Column(name = "trigger_name", nullable = false)
    private String triggerName; // 트리거 이름 (UK_1)

    @Column(name = "trigger_group", nullable = false)
    private String triggerGroup; // 트리거 그룹 (UK_1)

    @Column(name = "trigger_register", nullable = false)
    private String triggerRegister; // 트리거 등록자

    @Column(name = "repeat_count")
    private Integer repeatCount; // 반복 실행 횟수

    @Column(name = "repeat_interval")
    private Integer repeatInterval; // 반복 실행 간격

    @Column(name = "repeat_expression")
    private String repeatExpression; // 반복 실행 표현식

    @Column(name = "prev_fire_time")
    private LocalDateTime prevFireTime; // 이전 실행 시간

    @Column(name = "next_fire_time")
    private LocalDateTime nextFireTime; // 다음 실행 시간

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @Column(name = "stopTime", nullable = false)
    private LocalDateTime stopTime; // 종료 시간

    @Enumerated(value = EnumType.STRING)
    @Column(name = "class_type", nullable = false)
    private TriggerClassType classType; // 트리거 실행 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "policy_type", nullable = false)
    private TriggerPolicyType policyType; // 트리거 정책 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state_type", nullable = false)
    private ScheduleStateType stateType; // 트리거 상태 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private ScheduleStoreType storeType; // 트리거 등록 유형

    @Builder(access = AccessLevel.PRIVATE)
    private TriggerSummary(
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        String triggerRegister,
        Integer repeatCount,
        Integer repeatInterval,
        String repeatExpression,
        LocalDateTime prevFireTime,
        LocalDateTime nextFireTime,
        LocalDateTime startTime,
        LocalDateTime stopTime,
        TriggerClassType classType,
        TriggerPolicyType policyType,
        ScheduleStoreType storeType,
        ScheduleStateType stateType
    ) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.triggerRegister = triggerRegister;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.repeatExpression = repeatExpression;
        this.prevFireTime = prevFireTime;
        this.nextFireTime = nextFireTime;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.classType = classType;
        this.policyType = policyType;
        this.stateType = stateType;
        this.storeType = storeType;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static TriggerSummary create(
            String jobName,
            String jobGroup,
            String triggerName,
            String triggerGroup,
            String triggerRegister,
            LocalDateTime startTime,
            LocalDateTime stopTime,
            TriggerClassType classType,
            TriggerPolicyType policyType
    ) {
        return TriggerSummary.builder()
                .jobName(jobName)
                .jobGroup(jobGroup)
                .triggerName(triggerName)
                .triggerGroup(triggerGroup)
                .triggerRegister(triggerRegister)
                .startTime(startTime)
                .stopTime(stopTime)
                .classType(classType)
                .policyType(policyType)
                .stateType(ScheduleStateType.SCHEDULED)
                .storeType(ScheduleStoreType.STORED)
                .build();
    }

    /**
     * TriggerSummary 객체의 값을 복사한다.
     * <p> : 이전, 다음 실행 시간은 기존 값을 유지한다.
     */
    public void synchronize(TriggerSummary triggerProps) {
        switch (triggerProps.getClassType()) {
            case SP -> updateSimpleProps(triggerProps.getRepeatCount(), triggerProps.getRepeatInterval());
            case CR -> updateCronProps(triggerProps.getRepeatExpression());
        }

        updateStateType(ScheduleStateType.UPDATED);
        updateClassType(triggerProps.getClassType());
        updatePolicyType(triggerProps.getPolicyType());
        updateTriggerTime(triggerProps.getStartTime(), triggerProps.getStopTime());
    }

    /**
     * TriggerSummary 객체의 반복 값을 세팅한다.
     */
    public TriggerSummary setSimpleProps(Integer repeatCount, Integer repeatInterval) {
        if (classType != TriggerClassType.SP) {
            throw TriggerException.triggerClassTypeMismatch();
        }

        if (!Objects.equals(this.repeatCount, repeatCount)) {
            this.repeatCount = repeatCount;
        }

        if (!Objects.equals(this.repeatInterval, repeatInterval)) {
            this.repeatInterval = repeatInterval;
        }

        return this;
    }

    /**
     * TriggerSummary 객체의 반복 값을 세팅한다.
     */
    public TriggerSummary setCronProps(String repeatExpression) {
        if (classType != TriggerClassType.CR) {
            throw TriggerException.triggerClassTypeMismatch();
        }

        if (!Objects.equals(this.repeatExpression, repeatExpression)) {
            this.repeatExpression = repeatExpression;
        }

        return this;
    }

    /**
     * TriggerSummary 객체의 실행 유형을 수정한다.
     */
    private void updateClassType(TriggerClassType classType) {
        if (!Objects.equals(this.classType, classType)) {
            this.classType = classType;
        }
    }

    /**
     * TriggerSummary 객체의 반복 값을 수정한다.
     */
    private void updateSimpleProps(Integer repeatCount, Integer repeatInterval) {
        if (!Objects.equals(this.repeatCount, repeatCount)) {
            this.repeatCount = repeatCount;
        }

        if (!Objects.equals(this.repeatInterval, repeatInterval)) {
            this.repeatInterval = repeatInterval;
        }
    }

    /**
     * TriggerSummary 객체의 반복 값을 수정한다.
     */
    private void updateCronProps(String repeatExpression) {
        if (!Objects.equals(this.repeatExpression, repeatExpression)) {
            this.repeatExpression = repeatExpression;
        }
    }

    /**
     * TriggerSummary 객체의 이전, 다음 실행 시간 값을 수정한다.
     */
    public void updateTriggerFireTime(LocalDateTime prevFireTime, LocalDateTime nextFireTime) {
        if (!Objects.equals(this.prevFireTime, prevFireTime)) {
            this.prevFireTime = prevFireTime;
        }

        if (!Objects.equals(this.nextFireTime, nextFireTime)) {
            this.nextFireTime = nextFireTime;
        }
    }

    /**
     * TriggerSummary 객체의 실행 시작, 종료 시간 값을 수정한다.
     */
    public void updateTriggerTime(LocalDateTime startTime, LocalDateTime stopTime) {
        if (!Objects.equals(this.startTime, startTime)) {
            this.startTime = startTime;
        }

        if (!Objects.equals(this.stopTime, stopTime)) {
            this.stopTime = stopTime;
        }
    }

    /**
     * TriggerSummary 객체의 정책 유형을 수정한다.
     */
    private void updatePolicyType(TriggerPolicyType policyType) {
        if (!Objects.equals(this.policyType, policyType)) {
            this.policyType = policyType;
        }
    }

    /**
     * TriggerSummary 객체의 상태 유형을 수정한다.
     */
    public void updateStateType(ScheduleStateType stateType) {
        if (!Objects.equals(this.stateType, stateType)) {
            this.stateType = stateType;
        }
    }

    /**
     * TriggerSummary 객체를 삭제 처리한다.
     */
    public void delete() {
        this.stateType = ScheduleStateType.DELETED;
        this.storeType = ScheduleStoreType.DELETED;
    }

    // -----------------------------------------------------------------------------------------------------------------
}