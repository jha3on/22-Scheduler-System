package system.api.payload.shape;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

public interface HasScheduleKey {
    JobKey getJobKey();
    TriggerKey getTriggerKey();
}