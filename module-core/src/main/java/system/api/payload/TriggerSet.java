package system.api.payload;

import lombok.*;
import org.quartz.Trigger;
import system.core.entity.schedule.TriggerSummary;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerSet {
    private Trigger triggerDetail;
    private TriggerSummary triggerSummary;

    public static TriggerSet create(Trigger tDetail, TriggerSummary tSummary) {
        return TriggerSet.builder()
                .triggerDetail(tDetail)
                .triggerSummary(tSummary)
                .build();
    }
}