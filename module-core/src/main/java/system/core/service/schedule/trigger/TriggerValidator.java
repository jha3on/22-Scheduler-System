package system.core.service.schedule.trigger;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import system.api.payload.request.TriggerStoreRequest;
import system.api.payload.request.TriggerUpdateRequest;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TriggerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TriggerStoreRequest.class.isAssignableFrom(clazz)
            || TriggerUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof TriggerStoreRequest request) {
            validateRepeatNumber(request.getRepeatCount(), request.getRepeatInterval(), request.getClassType(), errors);
            validateRepeatExpression(request.getRepeatExpression(), request.getClassType(), errors);
            validatePolicy(request.getClassType(), request.getPolicyType(), errors);
            validateTime(request.getStartTime(), request.getStopTime(), errors);
        }

        if (target instanceof TriggerUpdateRequest request) {
            validateRepeatNumber(request.getRepeatCount(), request.getRepeatInterval(), request.getClassType(), errors);
            validateRepeatExpression(request.getRepeatExpression(), request.getClassType(), errors);
            validatePolicy(request.getClassType(), request.getPolicyType(), errors);
            validateTime(request.getStartTime(), request.getStopTime(), errors);
        }
    }

    /**
     * Simple 트리거 실행 조건을 검증한다.
     */
    private void validateRepeatNumber(Integer count, Integer interval, TriggerClassType classType, Errors errors) {
        if (Objects.equals(classType, TriggerClassType.SP)) {
            if (Objects.isNull(count)) {
                errors.rejectValue("repeatCount", "", "반복 실행 횟수를 입력하세요.");
            }

            if (!Objects.isNull(count) && count <= 0) {
                errors.rejectValue("repeatCount", "", "반복 실행 횟수를 확인하세요.");
            }

            if (Objects.isNull(interval)) {
                errors.rejectValue("repeatInterval", "", "반복 실행 간격을 입력하세요.");
            }

            if (!Objects.isNull(interval) && interval <= 0) {
                errors.rejectValue("repeatInterval", "", "반복 실행 횟수를 확인하세요.");
            }
        }
    }

    /**
     * Cron 트리거 실행 조건을 검증한다.
     */
    private void validateRepeatExpression(String expression, TriggerClassType classType, Errors errors) {
        if (Objects.equals(classType, TriggerClassType.CR)) {
            if (StringUtils.isEmpty(expression)) {
                errors.rejectValue("repeatExpression", "", "반복 실행 표현식을 입력하세요.");
            }

            if (!StringUtils.isEmpty(expression)) {
                try {
                    CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
                    parser.parse(expression).validate();
                } catch (IllegalArgumentException e) {
                    errors.rejectValue("repeatExpression", "", "반복 실행 표현식을 확인하세요.");
                }
            }
        }
    }

    /**
     * 트리거 정책 유형을 검증한다.
     */
    private void validatePolicy(TriggerClassType classType, TriggerPolicyType policyType, Errors errors) {
        String message = "트리거 실행 유형과 정책 유형을 확인하세요.";

        if (Objects.equals(classType, TriggerClassType.SP)) {
            if (!TriggerPolicyType.isSimplePolicy(policyType)) {
                errors.rejectValue("classType", "", message);
                errors.rejectValue("policyType", "", message);
            }
        }

        if (Objects.equals(classType, TriggerClassType.CR)) {
            if (!TriggerPolicyType.isCronPolicy(policyType)) {
                errors.rejectValue("classType", "", message);
                errors.rejectValue("policyType", "", message);
            }
        }
    }

    /**
     * 트리거 시간 정보를 검증한다.
     */
    private void validateTime(LocalDateTime start, LocalDateTime stop, Errors errors) {
        if (Objects.isNull(start)) {
            errors.rejectValue("startTime", "", "시작 시간을 입력하세요.");
        }

        if (Objects.isNull(stop)) {
            errors.rejectValue("stopTime", "", "종료 시간을 입력하세요.");
        }

        if (!Objects.isNull(start) && !Objects.isNull(stop)) {
            if (start.isAfter(stop)) {
                errors.rejectValue("startTime", "", "시작 시간은 종료 시간 이전으로 입력하세요.");
            }

            if (!start.isBefore(stop)) {
                errors.rejectValue("stopTime", "", "종료 시간은 시작 시간 이후로 입력하세요.");
            }
        }
    }
}