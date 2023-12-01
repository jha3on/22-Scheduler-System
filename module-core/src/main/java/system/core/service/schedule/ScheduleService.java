package system.core.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.request.*;
import system.api.payload.response.JobPathResponse;
import system.api.payload.response.TriggerPathResponse;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasTriggerKey;
import system.core.exception.JobException;
import system.core.exception.TriggerException;
import system.core.service.schedule.job.JobScheduleService;
import system.core.service.schedule.trigger.TriggerScheduleService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final JobScheduleService jobService;
    private final TriggerScheduleService triggerService;
    private final ScheduleQueryService scheduleQueryService;

    /**
     * 작업 스케줄을 등록한다.
     */
    @Transactional
    public JobPathResponse schedule(JobStoreRequest jRequest) {
        try {
            return JobPathResponse.create(jobService.store(jRequest));
        } catch (SchedulerException e) {
            throw JobException.jobCreation();
        }
    }

    /**
     * 작업 스케줄을 수정한다.
     */
    @Transactional
    public JobPathResponse reSchedule(JobUpdateRequest jRequest) {
        verifyJobOwnership(jRequest);

        try {
            return JobPathResponse.create(jobService.update(jRequest));
        } catch (SchedulerException e) {
            throw JobException.jobUpdation();
        }
    }

    /**
     * 작업 스케줄 상태를 수정한다.
     */
    @Transactional
    public JobPathResponse switchState(JobStateRequest jRequest) {
        verifyJobOwnership(jRequest);

        try {
            return JobPathResponse.create(jobService.updateState(jRequest));
        } catch (SchedulerException e) {
            throw JobException.jobUpdation();
        }
    }

    /**
     * 작업 스케줄을 삭제한다.
     * <p> : 작업과 연관된 트리거를 모두 삭제한다.
     */
    @Transactional
    public void unSchedule(JobDeleteRequest jRequest) {
        verifyJobOwnership(jRequest);

        try {
            jobService.delete(jRequest);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw JobException.jobDeletion();
        }
    }

    /**
     * 트리거 스케줄을 등록한다.
     */
    @Transactional
    public TriggerPathResponse schedule(TriggerStoreRequest tRequest) {
        verifyJobOwnership(tRequest);

        try {
            return TriggerPathResponse.create(triggerService.store(tRequest));
        } catch (SchedulerException e) {
            throw TriggerException.triggerCreation();
        }
    }

    /**
     * 트리거 스케줄을 수정한다.
     */
    @Transactional
    public TriggerPathResponse reSchedule(TriggerUpdateRequest tRequest) {
        verifyTriggerOwnership(tRequest);

        try {
            return TriggerPathResponse.create(triggerService.update(tRequest));
        } catch (SchedulerException e) {
            throw TriggerException.triggerUpdation();
        }
    }

    /**
     * 트리거 스케줄 상태를 수정한다.
     */
    @Transactional
    public TriggerPathResponse switchState(TriggerStateRequest tRequest) {
        verifyTriggerOwnership(tRequest);

        try {
            return TriggerPathResponse.create(triggerService.updateState(tRequest));
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw TriggerException.triggerUpdation();
        }
    }

    /**
     * 트리거 스케줄을 삭제한다.
     */
    @Transactional
    public void unSchedule(TriggerDeleteRequest tRequest) {
        verifyTriggerOwnership(tRequest);

        try {
            triggerService.delete(tRequest);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw TriggerException.triggerDeletion();
        }
    }

    /**
     * 작업 소유 권한을 확인한다.
     */
    private void verifyJobOwnership(HasJobKey jNames) {
        String jName = jNames.getJobName();
        String jGroup = jNames.getJobGroup();

        if (!scheduleQueryService.hasJobOwnership(jName, jGroup)) {
            throw JobException.jobRegisterMismatch();
        }
    }

    /**
     * 트리거 소유 권한을 확인한다.
     */
    private void verifyTriggerOwnership(HasTriggerKey tNames) {
        String tName = tNames.getTriggerName();
        String tGroup = tNames.getTriggerGroup();

        if (!scheduleQueryService.hasTriggerOwnership(tName, tGroup)) {
            throw TriggerException.triggerRegisterMismatch();
        }
    }
}