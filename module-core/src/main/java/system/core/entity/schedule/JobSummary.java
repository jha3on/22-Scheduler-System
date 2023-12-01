package system.core.entity.schedule;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import system.core.enums.schedule.JobClassType;
import system.core.enums.schedule.ScheduleStateType;
import system.core.enums.schedule.ScheduleStoreType;
import system.share.base.common.entity.AbstractEntity;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@DynamicUpdate
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"jobSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_job", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_name", "job_group"}),
})
public class JobSummary extends AbstractEntity {

    @Id
    @Column(name = "job_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobSeq; // 작업 번호 (PK_1)

    @Column(name = "job_name", nullable = false)
    private String jobName; // 작업 이름 (UK_1)

    @Column(name = "job_group", nullable = false)
    private String jobGroup; // 작업 그룹 (UK_1)

    @Column(name = "job_register", nullable = false)
    private String jobRegister; // 작업 등록자

    @Column(name = "class_name", nullable = false)
    private String className; // 작업 대상 클래스

    @Column(name = "class_data", nullable = false)
    private String classData; // 작업 대상 데이터

    @Column(name = "class_comment", nullable = false)
    private String classComment; // 작업 대상 설명

    @Enumerated(value = EnumType.STRING)
    @Column(name = "class_type", nullable = false)
    private JobClassType classType; // 작업 대상 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state_type", nullable = false)
    private ScheduleStateType stateType; // 작업 상태 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private ScheduleStoreType storeType; // 작업 등록 유형

    @Builder(access = AccessLevel.PRIVATE)
    private JobSummary(
        String jobName,
        String jobGroup,
        String jobRegister,
        String className,
        String classData,
        String classComment,
        JobClassType classType,
        ScheduleStateType stateType,
        ScheduleStoreType storeType
    ) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobRegister = jobRegister;
        this.className = className;
        this.classData = classData;
        this.classComment = classComment;
        this.classType = classType;
        this.stateType = stateType;
        this.storeType = storeType;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JobSummary create(
        String jobName,
        String jobGroup,
        String jobRegister,
        String className,
        String classData,
        String classComment,
        JobClassType classType
    ) {
        return JobSummary.builder()
                .jobName(jobName)
                .jobGroup(jobGroup)
                .jobRegister(jobRegister)
                .className(className)
                .classData(classData)
                .classComment(classComment)
                .classType(classType)
                .stateType(ScheduleStateType.SCHEDULED)
                .storeType(ScheduleStoreType.STORED)
                .build();
    }

    /**
     * JobSummary 객체의 값을 복사한다.
     */
    public void synchronize(JobSummary jSummary) {
        updateStateType(ScheduleStateType.UPDATED);
        updateClassType(jSummary.getClassType());
        updateClassProps(jSummary.getClassName(), jSummary.getClassData(), jSummary.getClassComment());
    }

    /**
     * JobSummary 객체의 대상 값을 수정한다.
     */
    private void updateClassProps(String className, String classData, String classComment) {
        if (!Objects.equals(this.className, className)) {
            this.className = className;
        }

        if (!Objects.equals(this.classData, classData)) {
            this.classData = classData;
        }

        if (!Objects.equals(this.classComment, classComment)) {
            this.classComment = classComment;
        }
    }

    /**
     * JobSummary 객체의 대상 유형을 수정한다.
     */
    private void updateClassType(JobClassType classType) {
        if (!Objects.equals(this.classType, classType)) {
            this.classType = classType;
        }
    }

    /**
     * JobSummary 객체의 상태 유형을 수정한다.
     */
    public void updateStateType(ScheduleStateType stateType) {
        if (!Objects.equals(this.stateType, stateType)) {
            this.stateType = stateType;
        }
    }

    /**
     * JobSummary 객체를 삭제 처리한다.
     */
    public void delete() {
        this.stateType = ScheduleStateType.DELETED;
        this.storeType = ScheduleStoreType.DELETED;
    }

    // -----------------------------------------------------------------------------------------------------------------
}