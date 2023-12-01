package system.core.service.schedule.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import system.api.payload.request.JobStoreRequest;
import system.api.payload.request.JobUpdateRequest;
import system.core.enums.schedule.JobClassType;
import system.core.exception.JobException;
import system.core.repository.query.JobQueryRepository;
import system.share.base.utility.MapperUtils;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobValidator implements Validator {
    private final JobClassLoader jobClassLoader;
    private final JobQueryRepository jobQueryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return JobStoreRequest.class.isAssignableFrom(clazz)
            || JobUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof JobStoreRequest request) {
            validateName(request.getJobName(), request.getJobGroup(), errors);
            validateClassName(request.getClassName(), request.getClassType(), errors);
            validateClassComment(request.getClassComment(), errors);
            validateClassData(request.getClassData(), errors);
        }

        if (target instanceof JobUpdateRequest request) {
            validateClassName(request.getClassName(), request.getClassType(), errors);
            validateClassComment(request.getClassComment(), errors);
            validateClassData(request.getClassData(), errors);
        }
    }

    private void validateName(String jName, String jGroup, Errors errors) {
        if (StringUtils.isEmpty(jName)) {
            errors.rejectValue("jobName", "", "작업 이름을 입력하세요.");
        }

        if (StringUtils.isEmpty(jGroup)) {
            errors.rejectValue("jobGroup", "", "작업 그룹을 입력하세요.");
        }

        if (jobQueryRepository.existsJobSummary(JobKey.jobKey(jName, jGroup))) {
            errors.rejectValue("jobName", "", "이미 등록된 작업 이름, 그룹입니다.");
            errors.rejectValue("jobGroup", "", "이미 등록된 작업 이름, 그룹입니다.");
        }
    }

    private void validateClassName(String className, JobClassType classType, Errors errors) {
        if (StringUtils.isEmpty(className)) {
            errors.rejectValue("className", "", "작업 대상 클래스를 입력하세요.");
        }

        try {
            jobClassLoader.get(className, classType);
        } catch (JobException e) {
            errors.rejectValue("className", "", "작업 대상 클래스를 확인하세요.");
        }
    }

    private void validateClassComment(String classComment, Errors errors) {
        if (StringUtils.isEmpty(classComment)) {
            errors.rejectValue("classComment", "", "작업 대상 설명을 입력하세요.");
        }
    }

    private void validateClassData(String classData, Errors errors) {
        if (StringUtils.isEmpty(classData)) return;

        try {
            MapperUtils.deserialize(classData, Map.class);
        } catch (JsonProcessingException e) {
            errors.rejectValue("classData", "", "작업 대상 데이터를 확인하세요.");
        }
    }
}