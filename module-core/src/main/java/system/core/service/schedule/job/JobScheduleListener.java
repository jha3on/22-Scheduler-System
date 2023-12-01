package system.core.service.schedule.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;
import system.core.service.schedule.ScheduleLogger;
import java.util.Objects;
import static system.core.enums.schedule.ScheduleStateType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduleListener implements JobListener {
    private final ScheduleLogger scheduleLogger;

    @Override
    public String getName() {
        return JobScheduleListener.class.getName();
    }

    /**
     * 작업 실행 준비
     * <p> : TriggerListener.vetoJobExecution() -> false 값일 때 호출된다.
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        scheduleLogger.jobCalled(context.getJobDetail().getKey(), EXECUTED);
    }

    /**
     * 작업 실행 중단
     * <p> : TriggerListener.vetoJobExecution() -> true 값일 때 호출된다.
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        scheduleLogger.jobCalled(context.getJobDetail().getKey(), REJECTED);
    }

    /**
     * 작업 실행 완료
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        scheduleLogger.jobCalled(context.getJobDetail().getKey(), COMPLETED);

        verifyLastExecution(context);
    }

    /**
     * 현재 실행된 작업의 마지막 실행 여부를 확인한다.
     * <p> : 잔여 실행이 없는 작업은 COMPLETED 상태를 로깅한다.
     * <p> : 잔여 실행이 남은 작업은 SCHEDULED 상태를 로깅한다.
     */
    private void verifyLastExecution(JobExecutionContext context) {
        if (!Objects.isNull(context.getNextFireTime())) {
            scheduleLogger.jobCalled(context.getJobDetail().getKey(), SCHEDULED);
        }
    }
}