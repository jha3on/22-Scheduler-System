package system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.request.JobStoreRequest;
import system.api.payload.request.TriggerStoreRequest;
import system.core.enums.schedule.JobClassType;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import system.core.service.schedule.ScheduleService;
import system.share.security.SecurityUtils;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummySchedules {
    private final ScheduleService scheduleService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Order(value = 2)
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSchedulesBySched();
        //createSchedulesBySysop();
    }

    /**
     * UserGradeType.SCHED 계정의 더미 스케줄을 생성한다.
     */
    private void createSchedulesBySched() {
        String userEmail = "sched1@test.com";
        String userPassword = "1";

        authenticate(userEmail, userPassword);
        createLocalJobs();
        createSimpleTriggers();
        SecurityUtils.clearAuthentication();
    }

    /**
     * UserGradeType.SYSOP 계정의 더미 스케줄을 생성한다.
     */
    private void createSchedulesBySysop() {
        String userEmail = "sysop1@test.com";
        String userPassword = "1";

        authenticate(userEmail, userPassword);
        createRemoteJobs();
        createCronTriggers();
        SecurityUtils.clearAuthentication();
    }

    private void authenticate(String userEmail, String userPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        SecurityUtils.setAuthentication(authenticationManager.authenticate(token));
    }

    private void createLocalJobs() {
        int count = 5;
        for (int i = 1; i <= count; i++) {
            JobStoreRequest request = JobStoreRequest.builder()
                .jobName(String.format("local-%d", i))
                .jobGroup(String.format("local-%d", i))
                .className("system.routine.LocalSampleJob")
                .classData("")
                .classComment("샘플")
                .classType(JobClassType.LOCAL)
                .build();

            scheduleService.schedule(request);
        }
    }

    private void createRemoteJobs() {
        int count = 5;
        for (int i = 1; i <= count; i++) {
            JobStoreRequest request = JobStoreRequest.builder()
                .jobName(String.format("remote-%d", i))
                .jobGroup(String.format("remote-%d", i))
                .className("system.remote.routine.RemoteSampleJob")
                .classData("")
                .classComment("샘플")
                .classType(JobClassType.REMOTE)
                .build();

            scheduleService.schedule(request);
        }
    }

    private void createSimpleTriggers() {
        int count = 5;
        for (int i = 1; i <= count; i++) {
            TriggerStoreRequest request = TriggerStoreRequest.builder()
                .jobName(String.format("local-%d", i))
                .jobGroup(String.format("local-%d", i))
                .repeatCount(10) // 10회 실행
                .repeatInterval(90) // 90초 간격
                .startTime(LocalDateTime.of(2024, 12, 1, 0, 0, 0))
                .stopTime(LocalDateTime.of(2024, 12, 15, 0, 0, 0))
                .classType(TriggerClassType.SP)
                .policyType(TriggerPolicyType.SP_NEXT_SCHEDULE_RETAIN_COUNT)
                .build();

            scheduleService.schedule(request);
        }
    }

    private void createCronTriggers() {
        int count = 5;
        for (int i = 1; i <= count; i++) {
            TriggerStoreRequest request = TriggerStoreRequest.builder()
                .jobName(String.format("remote-%d", i))
                .jobGroup(String.format("remote-%d", i))
                .repeatExpression("0 0/3 * * * ?") // 3분 간격
                .startTime(LocalDateTime.of(2024, 12, 1, 0, 0, 0))
                .stopTime(LocalDateTime.of(2024, 12, 15, 0, 0, 0))
                .classType(TriggerClassType.CR)
                .policyType(TriggerPolicyType.CR_NOW_SCHEDULE)
                .build();

            scheduleService.schedule(request);
        }
    }
}