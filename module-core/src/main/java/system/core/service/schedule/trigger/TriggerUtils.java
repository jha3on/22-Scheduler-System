package system.core.service.schedule.trigger;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import system.api.payload.TriggerSet;
import system.api.payload.shape.HasJobKey;
import system.api.payload.shape.HasTriggerKey;
import system.api.payload.shape.HasTriggerProperty;
import system.core.entity.schedule.TriggerSummary;
import system.core.enums.schedule.TriggerPolicyType;
import system.core.exception.TriggerException;
import system.share.security.SecurityUtils;
import system.share.base.utility.DateTimeUtils;
import system.share.base.utility.SystemUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TriggerUtils {

    /**
     * TriggerKey 객체를 가져온다.
     */
    public static TriggerKey getKey(HasTriggerKey tNames) {
        return TriggerKey.triggerKey(tNames.getTriggerName(), tNames.getTriggerGroup());
    }

    /**
     * TriggerKey 객체를 가져온다.
     */
    public static TriggerKey getKey(String tName, String tGroup) {
        return TriggerKey.triggerKey(tName, tGroup);
    }

    /**
     * TriggerKey 객체를 생성한다.
     * <p> - Trigger Name: {name}:{uid}
     * <p> - Trigger Group: {group}:{uid}
     */
    public static TriggerKey createKey(HasJobKey jNames) {
        String name = String.format("%s:%s", jNames.getJobName(), SystemUtils.unique());
        String group = String.format("%s:%s", jNames.getJobGroup(), SystemUtils.unique());

        return TriggerKey.triggerKey(name, group);
    }

    /**
     * TriggerKey 객체의 동등성을 비교한다.
     */
    public static Boolean equalsKey(TriggerKey o1, TriggerKey o2) {
        if (!o1.getGroup().equals(o2.getGroup())) return false;

        return o2.getName().equals(o2.getName());
    }

    /**
     * TriggerSet 객체를 생성한다.
     */
    public static TriggerSet createTriggerSet(JobKey jKey, TriggerKey tKey, HasTriggerProperty tProps) {
        Trigger tDetail = createTriggerDetail(jKey, tKey, tProps);
        TriggerSummary tSummary = createTriggerSummary(jKey, tDetail, tProps);

        return TriggerSet.create(tDetail, tSummary);
    }

    /**
     * Trigger 객체를 생성한다.
     */
    private static Trigger createTriggerDetail(JobKey jKey, TriggerKey tKey, HasTriggerProperty tProps) {
        verifyTriggerTime(tProps.getStartTime(), tProps.getStopTime());

        return switch (tProps.getClassType()) {
            case SP -> createSimpleTriggerDetail(jKey, tKey, tProps);
            case CR -> createCronTriggerDetail(jKey, tKey, tProps);
        };
    }

    /**
     * TriggerSummary 객체를 생성한다.
     */
    private static TriggerSummary createTriggerSummary(JobKey jKey, Trigger tDetail, HasTriggerProperty tProps) {
        TriggerSummary tSummary = TriggerSummary.create(
                jKey.getName(),
                jKey.getGroup(),
                tDetail.getKey().getName(),
                tDetail.getKey().getGroup(),
                SecurityUtils.getUserCode(),
                tProps.getStartTime(),
                tProps.getStopTime(),
                tProps.getClassType(),
                tProps.getPolicyType()
        );

        return switch (tProps.getClassType()) {
            case SP -> tSummary.setSimpleProps(tProps.getRepeatCount(), tProps.getRepeatInterval());
            case CR -> tSummary.setCronProps(tProps.getRepeatExpression());
        };
    }

    /**
     * SimpleTrigger 객체를 생성한다.
     */
    private static SimpleTrigger createSimpleTriggerDetail(JobKey jKey, TriggerKey tKey, HasTriggerProperty tProps) {
        verifyRepeatValue(tProps.getRepeatCount(), tProps.getRepeatInterval());

        ScheduleBuilder<SimpleTrigger> schedule = createSimpleSchedule(
                tProps.getRepeatCount(),
                tProps.getRepeatInterval(),
                tProps.getPolicyType());

        return TriggerBuilder.newTrigger()
                .withSchedule(schedule)
                .withIdentity(tKey.getName(), tKey.getGroup())
                .forJob(jKey.getName(), jKey.getGroup())
                .startAt(DateTimeUtils.toDate(tProps.getStartTime()))
                .endAt(DateTimeUtils.toDate(tProps.getStopTime()))
                .build();
    }

    /**
     * CronTrigger 객체를 생성한다.
     */
    private static CronTrigger createCronTriggerDetail(JobKey jKey, TriggerKey tKey, HasTriggerProperty tProps) {
        verifyRepeatExpression(tProps.getRepeatExpression());

        ScheduleBuilder<CronTrigger> schedule = createCronSchedule(
                tProps.getRepeatExpression(),
                tProps.getPolicyType());

        return TriggerBuilder.newTrigger()
                .withSchedule(schedule)
                .withIdentity(tKey.getName(), tKey.getGroup())
                .forJob(jKey.getName(), jKey.getGroup())
                .startAt(DateTimeUtils.toDate(tProps.getStartTime()))
                .endAt(DateTimeUtils.toDate(tProps.getStopTime()))
                .build();
    }

    /**
     * 트리거의 시작, 종료 시간을 검증한다.
     */
    public static void verifyTriggerTime(LocalDateTime start, LocalDateTime stop) {
        if (Objects.isNull(start) || Objects.isNull(stop)) throw TriggerException.invalidTriggerTimeValue();
        if (!start.isBefore(stop)) throw TriggerException.invalidTriggerTimeValue();
    }

    /**
     * Simple 트리거의 반복 값을 검증한다.
     */
    public static void verifyRepeatValue(Integer count, Integer interval) {
        if (Objects.isNull(count) || Objects.isNull(interval)) throw TriggerException.invalidTriggerTimeValue();
        if (count <= 0 || interval <= 0) throw TriggerException.invalidTriggerRepeatValue();
    }

    /**
     * Cron 트리거의 반복 표현식을 검증한다.
     */
    public static void verifyRepeatExpression(String expression) {
        try {
            CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
            parser.parse(expression).validate();
        } catch (IllegalArgumentException e) {
            throw TriggerException.invalidTriggerRepeatValue();
        }
    }

    /**
     * SimpleTrigger 객체의 스케줄 값을 생성한다.
     */
    private static ScheduleBuilder<SimpleTrigger> createSimpleSchedule(Integer count, Integer interval, TriggerPolicyType policyType) {
        return switch (policyType) {
            case SP_IGNORE_MISFIRE -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionIgnoreMisfires();
            case SP_NOW_SCHEDULE -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionFireNow();
            case SP_NOW_SCHEDULE_REDUCE_COUNT -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionNowWithExistingCount();
            case SP_NOW_SCHEDULE_RETAIN_COUNT -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionNowWithRemainingCount();
            case SP_NEXT_SCHEDULE_REDUCE_COUNT -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionNextWithExistingCount();
            case SP_NEXT_SCHEDULE_RETAIN_COUNT -> SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(count - 1)
                    .withIntervalInSeconds(interval)
                    .withMisfireHandlingInstructionNextWithRemainingCount();

            default -> throw TriggerException.unsupportedTriggerPolicyType();
        };
    }

    /**
     * CronTrigger 객체의 스케줄 값을 생성한다.
     */
    private static ScheduleBuilder<CronTrigger> createCronSchedule(String expression, TriggerPolicyType policyType) {
        return switch (policyType) {
            case CR_IGNORE_MISFIRE -> CronScheduleBuilder.cronSchedule(expression)
                    .inTimeZone(TimeZone.getDefault())
                    .withMisfireHandlingInstructionIgnoreMisfires();
            case CR_NOW_SCHEDULE -> CronScheduleBuilder.cronSchedule(expression)
                    .inTimeZone(TimeZone.getDefault())
                    .withMisfireHandlingInstructionFireAndProceed();
            case CR_PASS_MISFIRE -> CronScheduleBuilder.cronSchedule(expression)
                    .inTimeZone(TimeZone.getDefault())
                    .withMisfireHandlingInstructionDoNothing();

            default -> throw TriggerException.unsupportedTriggerPolicyType();
        };
    }
}