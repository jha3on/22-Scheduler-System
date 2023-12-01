package system.api.payload;

import lombok.*;
import org.quartz.JobDetail;
import system.core.entity.schedule.JobSummary;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobSet {
    private JobDetail jobDetail;
    private JobSummary jobSummary;

    public static JobSet create(JobDetail jDetail, JobSummary jSummary) {
        return JobSet.builder()
                .jobDetail(jDetail)
                .jobSummary(jSummary)
                .build();
    }
}