package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasTriggerKey;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerDeleteRequest implements HasJobKey, HasTriggerKey {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String triggerName; // 트리거 이름
    private String triggerGroup; // 트리거 그룹
}