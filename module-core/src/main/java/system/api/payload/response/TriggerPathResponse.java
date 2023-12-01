package system.api.payload.response;

import lombok.*;
import system.api.payload.ScheduleKeySet;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerPathResponse {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String triggerName; // 트리거 이름
    private String triggerGroup; // 트리거 그룹

    public static TriggerPathResponse create(ScheduleKeySet keySet) {
        return TriggerPathResponse.builder()
                .jobName(keySet.getJobKey().getName())
                .jobGroup(keySet.getJobKey().getGroup())
                .triggerName(keySet.getTriggerKey().getName())
                .triggerGroup(keySet.getTriggerKey().getGroup())
                .build();
    }
}