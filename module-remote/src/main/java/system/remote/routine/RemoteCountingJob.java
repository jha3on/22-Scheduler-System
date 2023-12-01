package system.remote.routine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.*;
import org.springframework.transaction.PlatformTransactionManager;
import system.core.enums.schedule.ScheduleStateType;
import system.core.service.schedule.ScheduleLogger;
import java.util.concurrent.TimeUnit;

@Slf4j
@DisallowConcurrentExecution
public class RemoteCountingJob implements Job {

    @Setter
    private ScheduleLogger scheduleLogger;

    @Setter
    private PlatformTransactionManager transactionManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StopWatch stopWatch = new StopWatch();
        JobKey jKey = context.getJobDetail().getKey();
        TriggerKey tKey = context.getTrigger().getKey();

        try {
            scheduleLogger.jobCalled(jKey, ScheduleStateType.PROCESSING);
            stopWatch.start();

            // [작업 처리 영역]
            long sleep = 5;
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                try {
                    char c = (char) ((int) (Math.random() * 26) + 65);
                    result.append(c);
                    TimeUnit.SECONDS.sleep(sleep);
                } catch (InterruptedException e) {
                    scheduleLogger.jobCalled(jKey, ScheduleStateType.ERRORED);
                }
            }
            // [작업 처리 영역]

            stopWatch.stop();
            String message = String.format("[10자리의 랜덤 문자열] 결과: %s, 처리 시간: %d (ms)", result, stopWatch.getTime());
            scheduleLogger.jobCalled(jKey, ScheduleStateType.SUCCESS, message);
        } catch (RuntimeException e) {
            String message = String.format("[10자리의 랜덤 문자열] 결과: %d, 처리 시간: %d (ms)", -1, stopWatch.getTime());
            scheduleLogger.jobCalled(jKey, ScheduleStateType.FAILURE, message);
        }
    }
}