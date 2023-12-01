package system.routine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.*;
import system.core.enums.schedule.ScheduleStateType;
import system.core.service.schedule.ScheduleLogger;

@Slf4j
@DisallowConcurrentExecution
public class LocalSampleJob implements Job {

    @Setter
    private ScheduleLogger scheduleLogger;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StopWatch stopWatch = new StopWatch();
        JobKey jKey = context.getJobDetail().getKey();
        TriggerKey tKey = context.getTrigger().getKey();

        try {
            scheduleLogger.jobCalled(jKey, ScheduleStateType.PROCESSING);
            stopWatch.start();

            // [작업 처리 영역]
            long sum = 0;
            for (long i = 1; i < 999_999_999; i++) {
                sum += i;
            }
            // [작업 처리 영역]

            stopWatch.stop();
            String message = String.format("[1, 999,999,999] 합계: %d, 처리 시간: %d (ms)", sum, stopWatch.getTime());
            scheduleLogger.jobCalled(jKey, ScheduleStateType.SUCCESS, message);
        } catch (RuntimeException e) {
            String message = String.format("[1, 999,999,999] 합계: %d, 처리 시간: %d (ms)", -1, stopWatch.getTime());
            scheduleLogger.jobCalled(jKey, ScheduleStateType.FAILURE, message);
        }
    }
}