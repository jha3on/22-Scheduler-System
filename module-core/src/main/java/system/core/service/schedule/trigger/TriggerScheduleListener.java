package system.core.service.schedule.trigger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import system.core.service.schedule.ScheduleLogger;
import system.share.base.utility.DateTimeUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import static system.core.enums.schedule.ScheduleStateType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TriggerScheduleListener implements TriggerListener {
    private final ScheduleLogger scheduleLogger;

    @Override
    public String getName() {
        return TriggerScheduleListener.class.getName();
    }

    /**
     * 트리거 실행 시작
     * <p> : vetoJobExecution(false) -> jobToBeExecuted() -> execute() -> jobWasExecuted() -> triggerComplete()
     * <p> : vetoJobExecution(true) -> jobExecutionVetoed()
     */
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        scheduleLogger.triggerCalled(trigger.getJobKey(), trigger.getKey(), EXECUTED);
    }

    /**
     * 트리거 실행 중단
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // if (...) { return true; }
        return false;
    }

    /**
     * 트리거 실행 실패
     */
    @Override
    public void triggerMisfired(Trigger trigger) {
        scheduleLogger.triggerCalled(trigger.getJobKey(), trigger.getKey(), ERRORED);
    }

    /**
     * 트리거 실행 완료
     */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction order) {
        scheduleLogger.triggerCalled(trigger.getJobKey(), trigger.getKey(), COMPLETED);

        verifyLastTrigger(trigger);
    }

    /**
     * 현재 실행된 트리거의 마지막 실행 여부를 확인한다.
     * <p> : 잔여 실행이 없는 트리거는 TERMINATED 상태를 로깅한다.
     * <p> : 잔여 실행이 남은 트리거는 SCHEDULED 상태를 로깅한다.
     */
    private void verifyLastTrigger(Trigger trigger) {
        JobKey jKey = trigger.getJobKey();
        TriggerKey tKey = trigger.getKey();

        if (Objects.isNull(trigger.getNextFireTime())) {
            scheduleLogger.triggerCalled(jKey, tKey, TERMINATED);
        } else {
            LocalDateTime prevFireTime = DateTimeUtils.toDateTime(trigger.getPreviousFireTime());
            LocalDateTime nextFireTime = DateTimeUtils.toDateTime(trigger.getNextFireTime());
            scheduleLogger.triggerCalled(jKey, tKey, prevFireTime, nextFireTime, SCHEDULED);
        }
    }
}