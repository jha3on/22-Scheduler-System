package system.core.service.schedule.trigger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.ScheduleKeySet;
import system.api.payload.TriggerSet;
import system.api.payload.request.TriggerDeleteRequest;
import system.api.payload.request.TriggerStateRequest;
import system.api.payload.request.TriggerStoreRequest;
import system.api.payload.request.TriggerUpdateRequest;
import system.core.enums.schedule.ScheduleStateType;
import system.core.exception.JobException;
import system.core.exception.TriggerException;
import system.core.service.schedule.ScheduleLogger;
import system.core.service.schedule.ScheduleSupporter;
import system.core.service.schedule.job.JobUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerScheduleService {
    private final Scheduler scheduler;
    private final ScheduleLogger scheduleLogger;
    private final ScheduleSupporter scheduleSupporter;

    /**
     * 트리거 스케줄을 등록한다.
     * <p> : 정지 상태의 작업은 정지 상태로 트리거를 등록한다.
     */
    @Transactional
    public ScheduleKeySet store(TriggerStoreRequest tRequest) throws SchedulerException {
        // JobKey, TriggerKey 객체를 가져온다.
        JobKey jKey = JobUtils.getKey(tRequest);
        TriggerKey tKey = TriggerUtils.createKey(tRequest);

        // JobKey, TriggerKey 객체로 트리거를 생성할 수 있는지 확인한다.
        verifyCreatable(jKey, tKey);

        // TriggerSet 객체를 생성, 등록한다.
        TriggerSet tSet = TriggerUtils.createTriggerSet(jKey, tKey, tRequest);
        scheduler.scheduleJob(tSet.getTriggerDetail());

        // 트리거 생성 로그를 반영한다.
        scheduleLogger.triggerStored(jKey, tKey, tSet.getTriggerSummary(), ScheduleStateType.SCHEDULED);

        return ScheduleKeySet.create(jKey, tKey);
    }

    /**
     * 트리거 스케줄을 수정한다.
     * <p> : 정지 상태의 작업은 정지 상태로 트리거를 수정한다.
     */
    @Transactional
    public ScheduleKeySet update(TriggerUpdateRequest tRequest) throws SchedulerException {
        // JobKey, TriggerKey 객체를 가져온다.
        JobKey jKey = JobUtils.getKey(tRequest);
        TriggerKey tKey = TriggerUtils.getKey(tRequest);

        // JobKey, TriggerKey 객체로 트리거를 수정할 수 있는지 확인한다.
        verifyUpdatable(jKey, tKey);

        // TriggerSet 객체를 생성, 등록한다.
        TriggerSet tSet = TriggerUtils.createTriggerSet(jKey, tKey, tRequest);
        scheduler.rescheduleJob(tKey, tSet.getTriggerDetail());

        // 트리거 수정 로그를 반영한다.
        scheduleLogger.triggerUpdated(jKey, tKey, tSet.getTriggerSummary(), ScheduleStateType.UPDATED);

        return ScheduleKeySet.create(jKey, tKey);
    }

    /**
     * 트리거 스케줄 상태를 수정한다.
     */
    @Transactional
    public ScheduleKeySet updateState(TriggerStateRequest tRequest) throws SchedulerException {
        // JobKey, Trigger 객체를 가져온다.
        JobKey jKey = JobUtils.getKey(tRequest);
        TriggerKey tKey = TriggerUtils.getKey(tRequest);

        // JobKey, TriggerKey 객체로 트리거 상태를 수정할 수 있는지 확인한다.
        verifyUpdatable(jKey, tKey);

        // 트리거의 상태를 수정하고 로그를 반영한다.
        switch (tRequest.getStateType()) {
            case PAUSED -> {
                scheduler.pauseTrigger(tKey);
                scheduleLogger.triggerStateUpdated(jKey, tKey, ScheduleStateType.PAUSED);
            }
            case RESUMED -> {
                scheduler.resumeTrigger(tKey);
                scheduleLogger.triggerStateUpdated(jKey, tKey, ScheduleStateType.RESUMED);
            }
        }

        return ScheduleKeySet.create(jKey, tKey);
    }

    /**
     * 트리거 스케줄을 삭제한다.
     */
    @Transactional
    public void delete(TriggerDeleteRequest tRequest) throws SchedulerException {
        // JobKey, Trigger 객체를 가져온다.
        JobKey jKey = JobUtils.getKey(tRequest);
        TriggerKey tKey = TriggerUtils.getKey(tRequest);

        // JobKey, TriggerKey 객체로 트리거를 삭제할 수 있는지 확인한다.
        verifyDeletable(jKey, tKey);

        // TriggerKey 객체로 트리거를 삭제한다.
        scheduler.unscheduleJob(tKey);

        // 트리거 삭제 로그를 반영한다.
        scheduleLogger.triggerDeleted(jKey, tKey);
    }

    /**
     * 트리거를 생성할 수 있는 상태인지 확인한다.
     */
    private void verifyCreatable(JobKey jKey, TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 작업 등록 유무를 확인한다.
        if (!scheduleSupporter.existsJob(jKey)) {
            throw JobException.jobNotFound();
        }

        // 스케줄러에서 트리거 이름 중복을 확인한다.
        if (scheduleSupporter.existsTrigger(tKey)) {
            throw TriggerException.triggerDuplication();
        }
    }

    /**
     * 트리거를 수정할 수 있는 상태인지 확인한다.
     */
    private void verifyUpdatable(JobKey jKey, TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 작업 등록 유무를 확인한다.
        if (!scheduleSupporter.existsJob(jKey)) {
            System.out.println(jKey);
            throw JobException.jobNotFound();
        }

        // 스케줄러에서 트리거 등록 유무를 확인한다.
        if (!scheduleSupporter.existsTrigger(tKey)) {
            throw TriggerException.triggerNotFound();
        }

        // 스케줄러에서 트리거 실행 상태를 확인한다.
        if (scheduleSupporter.isTriggerRunning(jKey, tKey)) {
            throw TriggerException.triggerUpdation();
        }
    }

    /**
     * 트리거를 삭제할 수 있는 상태인지 확인한다.
     */
    private void verifyDeletable(JobKey jKey, TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 작업 등록 유무를 확인한다.
        if (!scheduleSupporter.existsJob(jKey)) {
            throw JobException.jobNotFound();
        }

        // 스케줄러에서 트리거 등록 유무를 확인한다.
        if (!scheduleSupporter.existsTrigger(tKey)) {
            throw TriggerException.triggerNotFound();
        }

        // 스케줄러에서 트리거 실행 상태를 확인한다.
        if (scheduleSupporter.isTriggerRunning(jKey, tKey)) {
            throw TriggerException.triggerDeletion();
        }
    }

    /**
     * 트리거 상태를 변경할 수 있는 상태인지 확인한다.
     */
    private void verifySwitchable(JobKey jKey, TriggerKey tKey) throws SchedulerException {

    }
}