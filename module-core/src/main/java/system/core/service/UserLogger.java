package system.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.UserLogSet;
import system.core.entity.UserLog;
import system.core.entity.schedule.JobSummary;
import system.core.entity.schedule.TriggerSummary;
import system.core.enums.UserLogType;
import system.core.repository.UserLogRepository;
import system.share.security.SecurityUtils;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogger {
    private final UserLogRepository userLogRepository;

    /**
     * 사용자의 작업 등록 액션을 로깅한다.
     */
    @Transactional
    public void jobStored(JobSummary jSummary) {
        userLogRepository.save(set(jSummary, UserLogType.STORED));
    }

    /**
     * 사용자의 작업 수정 액션을 로깅한다.
     */
    @Transactional
    public void jobUpdated(JobSummary jSummary) {
        userLogRepository.save(set(jSummary, UserLogType.UPDATED));
    }

    /**
     * 사용자의 작업 수정 액션을 로깅한다.
     */
    @Transactional
    public void jobUpdated(JobSummary jSummary, List<TriggerSummary> tSummaries) {
        userLogRepository.saveAll(set(jSummary, tSummaries, UserLogType.UPDATED));
    }

    /**
     * 사용자의 작업 삭제 액션을 로깅한다.
     */
    @Transactional
    public void jobDeleted(JobSummary jSummary) {
        userLogRepository.save(set(jSummary, UserLogType.DELETED));
    }

    /**
     * 사용자의 작업 삭제 액션을 로깅한다.
     */
    @Transactional
    public void jobDeleted(JobSummary jSummary, List<TriggerSummary> tSummaries) {
        userLogRepository.saveAll(set(jSummary, tSummaries, UserLogType.DELETED));
    }

    /**
     * 사용자의 트리거 등록 액션을 로깅한다.
     */
    @Transactional
    public void triggerStored(JobSummary jSummary, TriggerSummary tSummary) {
        userLogRepository.save(set(jSummary, tSummary, UserLogType.STORED));
    }

    /**
     * 사용자의 트리거 수정 액션을 로깅한다.
     */
    @Transactional
    public void triggerUpdated(JobSummary jSummary, TriggerSummary tSummary) {
        userLogRepository.save(set(jSummary, tSummary, UserLogType.UPDATED));
    }

    /**
     * 사용자의 트리거 삭제 액션을 로깅한다.
     */
    @Transactional
    public void triggerDeleted(JobSummary jSummary, TriggerSummary tSummary) {
        userLogRepository.save(set(jSummary, tSummary, UserLogType.DELETED));
    }

    /**
     * UserLog 객체를 생성한다.
     */
    private UserLog set(JobSummary jSummary, UserLogType logType) {
        return UserLog.create(SecurityUtils.getUser(), jSummary, UserLogSet.job(logType));
    }

    /**
     * UserLog 객체를 생성한다.
     */
    private UserLog set(JobSummary jSummary, TriggerSummary tSummary, UserLogType logType) {
        return UserLog.create(SecurityUtils.getUser(), jSummary, tSummary, UserLogSet.trigger(logType));
    }

    /**
     * UserLog 컬렉션을 생성한다.
     */
    private List<UserLog> set(JobSummary jSummary, List<TriggerSummary> tSummaries, UserLogType logType) {
        return UserLog.create(SecurityUtils.getUser(), jSummary, tSummaries, UserLogSet.trigger(logType));
    }
}