package system.core.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import system.core.entity.User;
import system.core.enums.UserGradeType;
import system.core.enums.UserRoleType;
import system.core.exception.JobException;
import system.core.exception.TriggerException;
import system.core.service.schedule.job.JobUtils;
import system.core.service.schedule.trigger.TriggerUtils;
import system.share.security.SecurityUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleSupporter {
    private final Scheduler scheduler;
    private final ScheduleLogger scheduleLogger;

    /**
     * JobKey 객체로 작업 등록 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsJob(JobKey jKey) throws SchedulerException {
        return scheduler.checkExists(jKey);
    }

    /**
     * TriggerKey 객체로 트리거 등록 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsTrigger(TriggerKey tKey) throws SchedulerException {
        return scheduler.checkExists(tKey);
    }

    /**
     * JobKey 객체로 작업 정보를 가져온다.
     */
    @Transactional(readOnly = true)
    public JobDetail getJobDetail(JobKey jKey) throws SchedulerException {
        if (!existsJob(jKey)) {
            throw JobException.jobNotFound();
        }

        return scheduler.getJobDetail(jKey);
    }

    /**
     * TriggerKey 객체로 트리거 정보를 가져온다.
     */
    @Transactional(readOnly = true)
    public Trigger getTriggerDetail(TriggerKey tKey) throws SchedulerException {
        if (!existsTrigger(tKey)) {
            throw TriggerException.triggerNotFound();
        }

        return scheduler.getTrigger(tKey);
    }

    /**
     * JobKey 객체로 작업과 연관된 트리거 목록을 가져온다.
     */
    @Transactional(readOnly = true)
    public List<? extends Trigger> getTriggerDetails(JobKey jKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jKey);
    }

    /**
     * JobKey 객체로 작업과 연관된 트리거 키 목록을 가져온다.
     */
    @Transactional(readOnly = true)
    public List<TriggerKey> getTriggerKeys(JobKey jKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jKey).stream()
                .map(trigger -> trigger.getKey())
                .collect(Collectors.toList());
    }

    /**
     * JobKey 객체로 작업이 실행 상태인지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isJobRunning(JobKey jKey) throws SchedulerException {
        // 스케줄러에서 실행 중인 작업 목록을 가져온다.
        List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();

        // 실행 중인 작업 목록을 확인한다.
        if (isJobsEmpty(jobs)) {
            return false;
        }

        boolean result = false;
        for (JobExecutionContext job : jobs) {
            JobKey targetKey = job.getJobDetail().getKey();

            if (JobUtils.equalsKey(jKey, targetKey)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * TriggerKey 객체로 트리거가 실행 상태인지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isTriggerRunning(JobKey jKey, TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 작업과 연관된 트리거 목록을 가져온다.
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jKey);

        // 작업과 연관된 트리거 목록을 확인한다.
        if (isTriggersEmpty(triggers)) {
            return false;
        }

        boolean result = false;
        for (Trigger trigger : triggers) {
            TriggerKey targetKey = trigger.getKey();

            if (TriggerUtils.equalsKey(tKey, targetKey)) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(targetKey);

                if (Objects.equals(triggerState, Trigger.TriggerState.BLOCKED)) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * JobExecutionContext 컬렉션이 비어 있는지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isJobsEmpty(List<JobExecutionContext> jobs) {
        return CollectionUtils.isEmpty(jobs);
    }

    /**
     * Trigger 컬렉션이 비어 있는지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isTriggersEmpty(List<? extends Trigger> triggers) {
        return CollectionUtils.isEmpty(triggers);
    }

    /**
     * Trigger 컬렉션이 비어 있는지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isTriggersEmpty(JobKey jKey) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jKey);

        return CollectionUtils.isEmpty(triggers);
    }

    /**
     * 작업의 소유권을 확인한다.
     * <p> : 시스템 운영 권한을 가진 사용자를 포함한다.
     */
    @Transactional(readOnly = true)
    public Boolean isMatchJobRegister(JobKey jKey) {
        User user = SecurityUtils.getUser();

        if (UserGradeType.hasRoleTypes(user.getGradeType(), UserRoleType.O)) {
            return true;
        }

        String currentUser = user.getUserCode();
        String jobRegister = scheduleLogger
                .getJobSummary(jKey)
                .getJobRegister();

        return Objects.equals(currentUser, jobRegister);
    }

    /**
     * 트리거의 소유권을 확인한다.
     * <p> : 시스템 운영 권한을 가진 사용자를 포함한다.
     */
    @Transactional(readOnly = true)
    public Boolean isMatchTriggerRegister(JobKey jKey, TriggerKey tKey) {
        User user = SecurityUtils.getUser();

        if (UserGradeType.hasRoleTypes(user.getGradeType(), UserRoleType.O)) {
            return true;
        }

        String currentUser = user.getUserCode();
        String triggerRegister = scheduleLogger
                .getTriggerSummary(jKey, tKey)
                .getTriggerRegister();

        return Objects.equals(currentUser, triggerRegister);
    }




    /**
     * JobKey 객체로 작업이 정지 상태인지 확인한다.
     * <p> : 연관된 모든 트리거가 1개일 경우는 제외한다. (1번째 트리거를 등록하는 것이므로 제외한다.)
     * <p> : 연관된 모든 트리거가 정지 상태인지 확인한다. (지정된 트리거를 제외한 나머지를 확인한다.)
     */
    @Transactional(readOnly = true)
    public Boolean isJobPaused(JobKey jKey, TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 작업과 연관된 트리거 목록을 가져온다.
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jKey);

        boolean result = false;
        for (Trigger trigger : triggers) {
            TriggerKey targetKey = trigger.getKey();
            if (TriggerUtils.equalsKey(tKey, targetKey)) {
                continue;
            }

            Trigger.TriggerState triggerState = scheduler.getTriggerState(targetKey);
            result = Objects.equals(triggerState, Trigger.TriggerState.PAUSED);
        }

        return result;
    }

    /**
     * TriggerKey 객체로 트리거가 정지 상태인지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isTriggerPaused(TriggerKey tKey) throws SchedulerException {
        // 스케줄러에서 트리거 상태를 가져온다.
        Trigger.TriggerState triggerState = scheduler.getTriggerState(tKey);

        return Objects.equals(triggerState, Trigger.TriggerState.PAUSED);
    }

    /**
     * TriggerKey 객체로 트리거가 정지 상태인지 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean isTriggersPaused(List<TriggerKey> tKeys) throws SchedulerException {
        boolean result = false;
        for (TriggerKey tKey : tKeys) {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(tKey);

            result = Objects.equals(triggerState, Trigger.TriggerState.PAUSED);
        }

        return result;
    }
}