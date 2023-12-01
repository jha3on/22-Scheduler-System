package system.api.payload.response;

import lombok.*;
import system.api.payload.ScheduleKeySet;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPathResponse {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹

    public static JobPathResponse create(ScheduleKeySet keySet) {
        return JobPathResponse.builder()
                .jobName(keySet.getJobKey().getName())
                .jobGroup(keySet.getJobKey().getGroup())
                .build();
    }
}