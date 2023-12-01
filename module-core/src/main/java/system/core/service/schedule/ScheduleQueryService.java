package system.core.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.JobLogQuery;
import system.api.payload.query.JobQuery;
import system.api.payload.query.TriggerLogQuery;
import system.api.payload.query.TriggerQuery;
import system.api.payload.request.JobSearchRequest;
import system.api.payload.request.TriggerSearchRequest;
import system.core.enums.UserGradeType;
import system.core.enums.schedule.ScheduleStateType;
import system.core.exception.JobLogException;
import system.core.exception.TriggerLogException;
import system.core.repository.query.JobLogQueryRepository;
import system.core.repository.query.JobQueryRepository;
import system.core.repository.query.TriggerLogQueryRepository;
import system.core.repository.query.TriggerQueryRepository;
import system.core.service.schedule.job.JobUtils;
import system.core.service.schedule.trigger.TriggerUtils;
import system.share.security.SecurityUtils;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleQueryService {
    private final JobQueryRepository jQueryRepository;
    private final JobLogQueryRepository jLogQueryRepository;
    private final TriggerQueryRepository tQueryRepository;
    private final TriggerLogQueryRepository tLogQueryRepository;

    /**
     * JobSummary 객체의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsJobSummary(String jName, String jGroup) {
        return jQueryRepository.existsJobSummary(JobKey.jobKey(jName, jGroup));
    }

    /**
     * JobSummary.jobRegister 값을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean hasJobOwnership(String jName, String jGroup) {
        JobKey jKey = JobUtils.getKey(jName, jGroup);
        String userCode = SecurityUtils.getUserCode();
        UserGradeType userGrade = SecurityUtils.getUserGradeType();

        if (Objects.equals(userGrade, UserGradeType.SYSOP)) return true;

        return jQueryRepository.equalsJobRegister(jKey, userCode);
    }

    /**
     * TriggerSummary -> JobSummary.jobRegister 값을 확인한다.
     * <p> : 작업 소유자는 트리거를 관리할 수 있다.
     */
    @Transactional(readOnly = true)
    public Boolean hasTriggerOwnership(String tName, String tGroup) {
        TriggerKey tKey = TriggerUtils.getKey(tName, tGroup);
        String userCode = SecurityUtils.getUserCode();
        UserGradeType userGrade = SecurityUtils.getUserGradeType();

        if (Objects.equals(userGrade, UserGradeType.SYSOP)) return true;

        return tQueryRepository.equalsJobRegister(tKey, userCode);
    }

    /**
     * JobKey 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public JobKey getJobKey(Long jSeq) {
        JobQuery result = getJobSummary(jSeq);

        return JobKey.jobKey(result.getJobName(), result.getJobGroup());
    }

    /**
     * JobSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public JobQuery getJobSummary(Long jSeq) {
        return jQueryRepository
                .getJobSummary(jSeq)
                .orElseThrow(() -> JobLogException.jobLogNotFound());
    }

    /**
     * TriggerSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public TriggerQuery getTriggerSummary(Long tSeq) {
        return tQueryRepository
                .getTriggerSummary(tSeq)
                .orElseThrow(() -> TriggerLogException.triggerLogNotFound());
    }

    /**
     * JobSummary 컬렉션의 상태별 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Long getJobSummaryCount(ScheduleStateType... stateTypes) {
        return jQueryRepository.getJobSummaryCount(stateTypes);
    }

    /**
     * JobSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobQuery> getJobSummaryList(JobSearchRequest request, Pageable pageable) {
        String sort = request.getSort().name();
        String sortBy = request.getSortBy().name();
        pageable = JpaUtils.set(pageable, sort, sortBy);

        return jQueryRepository.getJobSummaryList(request.getKeyword(), request.getSortBy(), pageable);
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerQuery> getTriggerSummaryList(TriggerSearchRequest request, Pageable pageable) {
        String sort = request.getSort().name();
        String sortBy = request.getSortBy().name();
        pageable = JpaUtils.set(pageable, sort, sortBy);

        return tQueryRepository.getTriggerSummaryList(request.getKeyword(), request.getSortBy(), pageable);
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerQuery> getTriggerSummaryList(Long jSeq, TriggerSearchRequest request, Pageable pageable) {
        String sort = request.getSort().name();
        String sortBy = request.getSortBy().name();
        pageable = JpaUtils.set(pageable, sort, sortBy);

        return tQueryRepository.getTriggerSummaryList(jSeq, request.getKeyword(), request.getSortBy(), pageable);
    }

    /**
     * JobLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public JobLogQuery getJobLog(Long logSeq) {
        return jLogQueryRepository
                .getJobLog(logSeq)
                .orElseThrow(() -> JobLogException.jobLogNotFound());
    }

    /**
     * JobLog 컬렉션의 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Long getJobLogCount() {
        return jLogQueryRepository.getJobLogCount();
    }

    /**
     * JobLog 컬렉션의 상위 10개를 조회한다.
     */
    @Transactional(readOnly = true)
    public List<JobLogQuery> getJobLogList() {
        return jLogQueryRepository.getJobLogList();
    }

    /**
     * JobLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobLogQuery> getJobLogList(Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return jLogQueryRepository.getJobLogList(pageable);
    }

    /**
     * JobLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobLogQuery> getJobLogList(Long jobSeq, Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return jLogQueryRepository.getJobLogList(jobSeq, pageable);
    }

    /**
     * TriggerLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public TriggerLogQuery getTriggerLog(Long logSeq) {
        return tLogQueryRepository
                .getTriggerLog(logSeq)
                .orElseThrow(() -> TriggerLogException.triggerLogNotFound());
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogList(Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return tLogQueryRepository.getTriggerLogList(pageable);
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogListByJob(Long jobSeq, Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return tLogQueryRepository.getTriggerLogListByJob(jobSeq, pageable);
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogListByTrigger(Long triggerSeq, Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return tLogQueryRepository.getTriggerLogListByTrigger(triggerSeq, pageable);
    }
}