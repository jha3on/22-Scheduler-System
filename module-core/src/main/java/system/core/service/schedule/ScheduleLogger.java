package system.core.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.ScheduleLogSet;
import system.core.entity.schedule.JobLog;
import system.core.entity.schedule.JobSummary;
import system.core.entity.schedule.TriggerLog;
import system.core.entity.schedule.TriggerSummary;
import system.core.enums.schedule.ScheduleStateType;
import system.core.exception.JobLogException;
import system.core.exception.TriggerLogException;
import system.core.repository.JobLogRepository;
import system.core.repository.JobSummaryRepository;
import system.core.repository.TriggerLogRepository;
import system.core.repository.TriggerSummaryRepository;
import system.core.repository.query.JobQueryRepository;
import system.core.repository.query.TriggerQueryRepository;
import system.core.service.UserLogger;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleLogger {
    private final UserLogger userLogger;

    private final JobLogRepository jLogRepository;
    private final JobQueryRepository jQueryRepository;
    private final JobSummaryRepository jSummaryRepository;

    private final TriggerLogRepository tLogRepository;
    private final TriggerQueryRepository tQueryRepository;
    private final TriggerSummaryRepository tSummaryRepository;

    /**
     * 작업 등록 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void jobStored(JobKey jKey, JobSummary jSummary, ScheduleStateType stateType) {
        if (existsJobSummary(jKey)) throw JobLogException.jobLogDuplication();

        jSummaryRepository.save(jSummary);
        jLogRepository.save(set(jSummary, stateType));
        userLogger.jobStored(jSummary);
    }

    /**
     * 트리거 등록 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void triggerStored(JobKey jKey, TriggerKey tKey, TriggerSummary tSummary, ScheduleStateType stateType) {
        if (existsTriggerSummary(jKey, tKey)) throw TriggerLogException.triggerLogDuplication();

        tSummaryRepository.save(tSummary);
        tLogRepository.save(set(tSummary, stateType));
        userLogger.triggerStored(getJobSummary(jKey), tSummary);
    }

    /**
     * 작업 수정 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void jobUpdated(JobKey jKey, JobSummary jProps, ScheduleStateType stateType) {
        JobSummary jSummary = getJobSummary(jKey);

        jSummary.synchronize(jProps);
        jLogRepository.save(set(jSummary, stateType));
        userLogger.jobUpdated(jSummary);
    }

    /**
     * 트리거 수정 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void triggerUpdated(JobKey jKey, TriggerKey tKey, TriggerSummary tProps, ScheduleStateType stateType) {
        JobSummary jSummary = getJobSummary(jKey);
        TriggerSummary tSummary = getTriggerSummary(jKey, tKey);

        tSummary.synchronize(tProps);
        tLogRepository.save(set(tSummary, stateType));
        userLogger.triggerUpdated(jSummary, tSummary);
    }

    /**
     * 작업 상태 수정 시, 스케줄 로그를 기록한다.
     * <p> : 연관된 트리거가 있을 경우, 트리거 상태 수정 로그를 기록한다.
     */
    @Transactional
    public void jobStateUpdated(JobKey jKey, List<TriggerKey> tKeys, ScheduleStateType stateType) {
        JobSummary jSummary = getJobSummary(jKey);
        List<TriggerSummary> tSummaries = getTriggerSummaries(jKey, tKeys);

        jSummary.updateStateType(stateType);
        jLogRepository.save(set(jSummary, stateType));

        if (tSummaries.isEmpty()) {
            userLogger.jobUpdated(jSummary);
        } else {
            triggersUpdated(tSummaries, stateType);
            userLogger.jobUpdated(jSummary, tSummaries);
        }
    }

    /**
     * 트리거 상태 수정 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void triggerStateUpdated(JobKey jKey, TriggerKey tKey, ScheduleStateType stateType) {
        JobSummary jSummary = getJobSummary(jKey);
        TriggerSummary tSummary = getTriggerSummary(jKey, tKey);

        tSummary.updateStateType(stateType);
        tLogRepository.save(set(tSummary, stateType));
        userLogger.triggerUpdated(jSummary, tSummary);
    }

    /**
     * 작업 삭제 시, 스케줄 로그를 기록한다.
     * <p> : 연관된 트리거가 있을 경우, 트리거 삭제 로그를 기록한다.
     */
    @Transactional
    public void jobDeleted(JobKey jKey, List<TriggerKey> tKeys) {
        JobSummary jSummary = getJobSummary(jKey);
        List<TriggerSummary> tSummaries = getTriggerSummaries(jKey, tKeys);

        jSummary.delete();
        jLogRepository.save(set(jSummary, ScheduleStateType.DELETED));

        if (tSummaries.isEmpty()) {
            userLogger.jobDeleted(jSummary);
        } else {
            triggersDeleted(tSummaries);
            userLogger.jobDeleted(jSummary, tSummaries);
        }
    }

    /**
     * 트리거 삭제 시, 스케줄 로그를 기록한다.
     * <p> : 연관된 트리거가 없을 경우, 작업 삭제 로그를 기록한다.
     */
    @Transactional
    public void triggerDeleted(JobKey jKey, TriggerKey tKey) {
        JobSummary jSummary = getJobSummary(jKey);
        TriggerSummary tSummary = getTriggerSummary(jKey, tKey);

        tSummary.delete();
        tLogRepository.save(set(tSummary, ScheduleStateType.DELETED));
        userLogger.triggerDeleted(jSummary, tSummary);
    }

    /**
     * 작업 이벤트 발생 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void jobCalled(JobKey jKey, ScheduleStateType stateType) {
        JobSummary jSummary = getJobSummary(jKey);

        jSummary.updateStateType(stateType);
        jLogRepository.save(set(jSummary, stateType));
    }

    /**
     * 작업 이벤트 발생 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void jobCalled(JobKey jKey, ScheduleStateType stateType, String message) {
        JobSummary jSummary = getJobSummary(jKey);

        jSummary.updateStateType(stateType);
        jLogRepository.save(set(jSummary, stateType, message));
    }

    /**
     * 트리거 이벤트 발생 시, 스케줄 로그를 기록한다.
     */
    @Transactional
    public void triggerCalled(JobKey jKey, TriggerKey tKey, ScheduleStateType stateType) {
        TriggerSummary tSummary = getTriggerSummary(jKey, tKey);

        tSummary.updateStateType(stateType);
        tLogRepository.save(set(tSummary, stateType));
    }

    /**
     * 트리거 이벤트 발생 시, 스케줄 로그를 갱신한다.
     */
    @Transactional
    public void triggerCalled(JobKey jKey, TriggerKey tKey, LocalDateTime prevFire, LocalDateTime nextFire, ScheduleStateType stateType) {
        TriggerSummary tSummary = getTriggerSummary(jKey, tKey);

        tSummary.updateStateType(stateType);
        tSummary.updateTriggerFireTime(prevFire, nextFire);
        tLogRepository.save(set(tSummary, stateType));
    }

    /**
     * JobSummary 객체의 등록 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsJobSummary(JobKey jKey) {
        return jQueryRepository.existsJobSummary(jKey);
    }

    /**
     * TriggerLogSummary 객체의 등록 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsTriggerSummary(JobKey jKey, TriggerKey tKey) {
        return tQueryRepository.existsTriggerSummary(jKey, tKey);
    }

    /**
     * JobSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public JobSummary getJobSummary(JobKey jKey) {
        return jQueryRepository.getJobSummary(jKey).orElseThrow(() -> JobLogException.jobLogNotFound());
    }

    /**
     * TriggerSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public TriggerSummary getTriggerSummary(JobKey jKey, TriggerKey tKey) {
        return tQueryRepository.getTriggerSummary(jKey, tKey).orElseThrow(() -> JobLogException.jobLogNotFound());
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<TriggerSummary> getTriggerSummaries(JobKey jKey, List<TriggerKey> tKeys) {
        return tQueryRepository.getTriggerSummaryList(jKey, tKeys);
    }

    /**
     * TriggerSummary 컬렉션을 수정한다.
     */
    @Transactional
    public void triggersUpdated(List<TriggerSummary> tSummaries, ScheduleStateType stateType) {
        tSummaries.forEach(tLogSummary -> tLogSummary.updateStateType(stateType));
        tLogRepository.saveAll(set(tSummaries, stateType));
    }

    /**
     * TriggerSummary 컬렉션을 삭제한다.
     */
    @Transactional
    public void triggersDeleted(List<TriggerSummary> tSummaries) {
        tSummaries.forEach(tLogSummary -> tLogSummary.delete());
        tLogRepository.saveAll(set(tSummaries, ScheduleStateType.DELETED));
    }

    /**
     * JobLog 객체를 생성한다.
     */
    private JobLog set(JobSummary jSummary, ScheduleStateType stateType) {
        return JobLog.create(jSummary, ScheduleLogSet.job(stateType));
    }

    /**
     * JobLog 객체를 생성한다.
     */
    private JobLog set(JobSummary jSummary, ScheduleStateType stateType, String message) {
        return JobLog.create(jSummary, ScheduleLogSet.job(stateType, message));
    }

    /**
     * TriggerLog 객체를 생성한다.
     */
    private TriggerLog set(TriggerSummary tSummary, ScheduleStateType stateType) {
        return TriggerLog.create(tSummary, ScheduleLogSet.trigger(stateType));
    }

    /**
     * TriggerLog 컬렉션을 생성한다.
     */
    private List<TriggerLog> set(List<TriggerSummary> tSummaries, ScheduleStateType stateType) {
        return TriggerLog.create(tSummaries, ScheduleLogSet.trigger(stateType));
    }
}