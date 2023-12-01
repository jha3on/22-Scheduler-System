package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasTriggerProperty;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerStoreRequest implements HasJobKey, HasTriggerProperty {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
    private Integer repeatCount; // 반복 실행 횟수
    private Integer repeatInterval; // 반복 실행 간격
    private String repeatExpression; // 반복 실행 표현식
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime stopTime; // 종료 시간
    private TriggerClassType classType; // 트리거 실행 유형
    private TriggerPolicyType policyType; // 트리거 정책 유형
}