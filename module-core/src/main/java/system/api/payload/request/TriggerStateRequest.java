package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasTriggerKey;
import system.core.enums.schedule.ScheduleStateType;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerStateRequest implements HasJobKey, HasTriggerKey {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String triggerName; // 트리거 이름
    private String triggerGroup; // 트리거 그룹
    private ScheduleStateType stateType; // 트리거 상태 유형
}