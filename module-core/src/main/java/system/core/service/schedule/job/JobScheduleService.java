package system.core.service.schedule.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.JobSet;
import system.api.payload.ScheduleKeySet;
import system.api.payload.request.JobDeleteRequest;
import system.api.payload.request.JobStateRequest;
import system.api.payload.request.JobStoreRequest;
import system.api.payload.request.JobUpdateRequest;
import system.core.enums.schedule.ScheduleStateType;
import system.core.exception.JobException;
import system.core.service.schedule.ScheduleLogger;
import system.core.service.schedule.ScheduleSupporter;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobScheduleService {
    private final Scheduler scheduler;
    private final ScheduleLogger scheduleLogger;
    private final ScheduleSupporter scheduleSupporter;
    private final JobClassLoader jobLoader;

    /**
     * 작업 스케줄을 등록한다.
     * <p> : 트리거를 갖지 않는 작업은 정지 상태로 작업을 등록한다.
     */
    @Transactional
    public ScheduleKeySet store(JobStoreRequest jRequest) throws SchedulerException {
        // JobKey 객체를 가져온다.
        JobKey jKey = JobUtils.createKey(jRequest);

        // JobKey 객체로 작업을 생성할 수 있는지 확인한다.
        verifyCreatable(jKey);

        // 작업 클래스를 가져온다.
        Class<? extends Job> jClass = jobLoader.get(jRequest.getClassName(), jRequest.getClassType());

        // JobSet 객체를 생성, 등록한다.
        JobSet jSet = JobUtils.createJobSet(jKey, jRequest, jClass);
        scheduler.addJob(jSet.getJobDetail(), true);

        // 작업 생성 로그를 반영한다.
        scheduleLogger.jobStored(jKey, jSet.getJobSummary(), ScheduleStateType.SCHEDULED);

        return ScheduleKeySet.create(jKey, null);
    }

    /**
     * 작업 스케줄을 수정한다.
     */
    @Transactional
    public ScheduleKeySet update(JobUpdateRequest jRequest) throws SchedulerException {
        // JobKey 객체를 가져온다.
        JobKey jKey = JobUtils.getKey(jRequest);
        List<? extends Trigger> tDetails = scheduleSupporter.getTriggerDetails(jKey);

        // JobKey 객체로 작업을 수정할 수 있는지 확인한다.
        verifyUpdatable(jKey);

        // 작업 클래스를 가져온다.
        Class<? extends Job> jClass = jobLoader.get(jRequest.getClassName(), jRequest.getClassType());

        // JobSet 객체를 생성, 등록한다.
        JobSet jSet = JobUtils.createJobSet(jKey, jRequest, jClass);
        reSchedule(jSet.getJobDetail(), tDetails);

        // 작업 수정 로그를 반영한다.
        scheduleLogger.jobUpdated(jKey, jSet.getJobSummary(), ScheduleStateType.UPDATED);

        return ScheduleKeySet.create(jKey, null);
    }

    /**
     * 작업 스케줄 상태를 수정한다.
     */
    @Transactional
    public ScheduleKeySet updateState(JobStateRequest jRequest) throws SchedulerException {
        // JobKey 객체, Trigger 컬렉션을 가져온다.
        JobKey jKey = JobUtils.getKey(jRequest);
        List<TriggerKey> tKeys = scheduleSupporter.getTriggerKeys(jKey);

        // JobKey 객체로 작업 상태를 수정할 수 있는지 확인한다.
        verifyUpdatable(jKey);
        verifySwitchable(jKey);

        // 작업, 연관된 트리거의 상태를 수정하고 로그를 반영한다.
        switch (jRequest.getStateType()) {
            case PAUSED -> {
                scheduler.pauseJob(jKey);
                scheduleLogger.jobStateUpdated(jKey, tKeys, ScheduleStateType.PAUSED);
            }
            case RESUMED -> {
                scheduler.resumeJob(jKey);
                scheduleLogger.jobStateUpdated(jKey, tKeys, ScheduleStateType.RESUMED);
            }
        }

        return ScheduleKeySet.create(jKey, null);
    }

    /**
     * 작업 스케줄을 삭제한다.
     */
    @Transactional
    public void delete(JobDeleteRequest jRequest) throws SchedulerException {
        // JobKey 객체, Trigger 컬렉션을 가져온다.
        JobKey jKey = JobUtils.getKey(jRequest);
        List<TriggerKey> tKeys = scheduleSupporter.getTriggerKeys(jKey);

        // JobKey 객체로 작업을 삭제할 수 있는지 확인한다.
        verifyDeletable(jKey);

        // JobKey 객체로 작업, 연관된 트리거를 삭제한다.
        scheduler.deleteJob(jKey);

        // 작업, 트리거 삭제 로그를 반영한다.
        scheduleLogger.jobDeleted(jKey, tKeys);
    }

    /**
     * 작업을 생성할 수 있는 상태인지 확인한다.
     */
    private void verifyCreatable(JobKey jKey) throws SchedulerException {
        // 스케줄러에서 작업 이름 중복을 확인한다.
        if (scheduleSupporter.existsJob(jKey)) {
            throw JobException.jobDuplication();
        }
    }

    /**
     * 작업을 수정할 수 있는 상태인지 확인한다.
     */
    private void verifyUpdatable(JobKey jKey) throws SchedulerException {
        // 스케줄러에서 작업 등록 유무를 확인한다.
        if (!scheduleSupporter.existsJob(jKey)) {
            throw JobException.jobNotFound();
        }

        // 스케줄러에서 작업 실행 상태를 확인한다.
        if (scheduleSupporter.isJobRunning(jKey)) {
            throw JobException.jobUpdation();
        }
    }

    /**
     * 작업을 삭제할 수 있는 상태인지 확인한다.
     */
    private void verifyDeletable(JobKey jKey) throws SchedulerException {
        // 스케줄러에서 작업 등록 유무를 확인한다.
        if (!scheduleSupporter.existsJob(jKey)) {
            throw JobException.jobNotFound();
        }

        // 스케줄러에서 작업 실행 상태를 확인한다.
        if (scheduleSupporter.isJobRunning(jKey)) {
            throw JobException.jobDeletion();
        }
    }

    /**
     * 작업 상태를 변경할 수 있는 상태인지 확인한다.
     * <p> : 연관된 트리거가 없는 작업은 상태를 변경할 수 없다.
     */
    private void verifySwitchable(JobKey jKey) throws SchedulerException {
        if (scheduleSupporter.isTriggersEmpty(jKey)) {
            throw JobException.jobSwitch();
        }
    }

    /**
     * 작업 수정 시, 연관된 트리거를 다시 연결한다.
     */
    private void reSchedule(JobDetail jDetail, List<? extends Trigger> tDetails) throws SchedulerException {
        if (tDetails.isEmpty()) {
            scheduler.addJob(jDetail, true);
        } else {
            for (Trigger tDetail : tDetails) {
                scheduler.scheduleJob(jDetail, Set.of(tDetail), true);
            }
        }
    }
}