package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;
import system.core.enums.schedule.ScheduleStateType;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobStateRequest implements HasJobKey {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private ScheduleStateType stateType; // 작업 상태 유형
}