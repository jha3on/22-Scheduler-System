package system.api.payload.request;

import lombok.*;
import system.api.payload.shape.HasJobKey;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobDeleteRequest implements HasJobKey {
    private String jobName; // 작업 이름
    private String jobGroup; // 작업 그룹
}