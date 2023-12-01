package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasJobProperty;
import system.core.enums.schedule.JobClassType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobStoreRequest implements HasJobKey, HasJobProperty {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private String className; // 작업 대상 클래스
    private String classData; // 작업 대상 데이터
    private String classComment; // 작업 대상 설명
    private JobClassType classType; // 작업 대상 유형
}