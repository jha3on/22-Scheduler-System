package system.api.payload.shape;

import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import java.time.LocalDateTime;

public interface HasTriggerProperty {
    Integer getRepeatCount();
    Integer getRepeatInterval();
    String getRepeatExpression();
    TriggerClassType getClassType();
    TriggerPolicyType getPolicyType();
    LocalDateTime getStartTime();
    LocalDateTime getStopTime();
}