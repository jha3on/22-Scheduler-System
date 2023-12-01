package system.api.payload;

import lombok.*;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import system.api.payload.shape.HasScheduleKey;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleKeySet implements HasScheduleKey {
    private JobKey jobKey;
    private TriggerKey triggerKey;

    public static ScheduleKeySet create(JobKey jKey, TriggerKey tKey) {
        return ScheduleKeySet.builder()
                .jobKey(jKey)
                .triggerKey(tKey)
                .build();
    }
}