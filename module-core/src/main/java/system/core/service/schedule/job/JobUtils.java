package system.core.service.schedule.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import system.api.payload.JobSet;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasJobProperty;
import system.core.entity.schedule.JobSummary;
import system.core.exception.JobException;
import system.share.security.SecurityUtils;
import system.share.base.utility.MapperUtils;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobUtils {

    /**
     * JobKey 객체를 가져온다.
     */
    public static JobKey getKey(HasJobKey jNames) {
        return JobKey.jobKey(jNames.getJobName(), jNames.getJobGroup());
    }

    /**
     * JobKey 객체를 가져온다.
     */
    public static JobKey getKey(String jName, String jGroup) {
        return JobKey.jobKey(jName, jGroup);
    }

    /**
     * JobKey 객체를 생성한다.
     * <p> - Job Name: {name}
     * <p> - Job Name: {group}
     */
    public static JobKey createKey(HasJobKey jNames) {
        String name = jNames.getJobName();
        String group = jNames.getJobGroup();

        return JobKey.jobKey(name, group);
    }

    /**
     * JobKey 객체의 동등성을 비교한다.
     */
    public static Boolean equalsKey(JobKey o1, JobKey o2) {
        if (!o1.getGroup().equals(o2.getGroup())) return false;

        return o2.getName().equals(o2.getName());
    }

    /**
     * JobSet 객체를 생성한다.
     */
    public static JobSet createJobSet(JobKey jKey, HasJobProperty jProps, Class<? extends Job> jClass) {
        JobDetail jDetail = createJobDetail(jKey, jProps, jClass);
        JobSummary jSummary = createJobSummary(jProps, jDetail);

        return JobSet.create(jDetail, jSummary);
    }

    /**
     * JobDetail 객체를 생성한다.
     * <p> : JobDetail 객체는 유지한다. (storeDurably: true)
     */
    private static JobDetail createJobDetail(JobKey jKey, HasJobProperty jProps, Class<? extends Job> jClass) {
        return JobBuilder.newJob(jClass)
                .withIdentity(jKey.getName(), jKey.getGroup())
                .withDescription(jProps.getClassComment())
                .usingJobData(createJobDataMap(jProps.getClassData()))
                .storeDurably(true)
                .build();
    }

    /**
     * JobSummary 객체를 생성한다.
     */
    private static JobSummary createJobSummary(HasJobProperty jProps, JobDetail jDetail) {
        return JobSummary.create(
                jDetail.getKey().getName(),
                jDetail.getKey().getGroup(),
                SecurityUtils.getUserCode(),
                jDetail.getJobClass().getName(), // 클래스 경로
                // jDetail.getJobClass().getSimpleName(), // 클래스 이름
                createJobDataJson(jDetail.getJobDataMap()),
                jDetail.getDescription(),
                jProps.getClassType());
    }

    /**
     * JobDataMap 객체를 생성한다.
     */
    private static JobDataMap createJobDataMap(String jClassData) {
        if (StringUtils.isEmpty(jClassData)) return new JobDataMap();

        try {
            return new JobDataMap(MapperUtils.deserialize(jClassData, Map.class));
        } catch (JsonProcessingException e) {
            throw JobException.jobSerialization();
        }
    }

    /**
     * JobDataMap 객체를 변환한다.
     */
    private static String createJobDataJson(JobDataMap jClassData) {
        try {
            return MapperUtils.serialize(jClassData);
        } catch (JsonProcessingException e) {
            throw JobException.jobSerialization();
        }
    }
}