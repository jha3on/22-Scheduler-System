package system.api.payload.shape;

import system.core.enums.schedule.JobClassType;

public interface HasJobProperty {
    String getClassName();
    String getClassData();
    String getClassComment();
    JobClassType getClassType();
}