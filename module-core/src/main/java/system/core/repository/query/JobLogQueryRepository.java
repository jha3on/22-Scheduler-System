package system.core.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.JobLogQuery;
import system.api.payload.query.QJobLogQuery;
import system.core.entity.schedule.QJobLog;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobLogQueryRepository {
    private final JPAQueryFactory query;
    private final QJobLog jLog = QJobLog.jobLog;

    /**
     * JobLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<JobLogQuery> getJobLog(Long logSeq) {
        return Optional.ofNullable(
                query.select(new QJobLogQuery(
                        jLog.logSeq,
                        jLog.jobSummary.jobSeq,
                        jLog.logResult,
                        jLog.logDetail,
                        jLog.logState,
                        jLog.logTime))
                     .from(jLog)
                     .where(jLog.logSeq.eq(logSeq))
                     .fetchOne());
    }

    /**
     * JobLog 컬렉션의 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Long getJobLogCount() {
        return query.select(jLog.count())
                .from(jLog)
                .fetchOne();
    }

    /**
     * JobLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<JobLogQuery> getJobLogList() {
        return query.select(new QJobLogQuery(
                    jLog.logSeq,
                    jLog.jobSummary.jobSeq,
                    jLog.logResult,
                    jLog.logState,
                    jLog.logTime))
                .from(jLog)
                .orderBy(jLog.logSeq.desc())
                .limit(10)
                .fetch();
    }

    /**
     * JobLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobLogQuery> getJobLogList(Pageable pageable) {
        Long total = query.select(jLog.count())
                .from(jLog)
                .fetchOne();

        List<JobLogQuery> results = query.select(new QJobLogQuery(
                    jLog.logSeq,
                    jLog.jobSummary.jobSeq,
                    jLog.logResult,
                    jLog.logState,
                    jLog.logTime))
                .from(jLog)
                .orderBy(jLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * JobLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobLogQuery> getJobLogList(Long jobSeq, Pageable pageable) {
        Long total = query.select(jLog.count())
                .from(jLog)
                .fetchOne();

        List<JobLogQuery> results = query.select(new QJobLogQuery(
                    jLog.logSeq,
                    jLog.jobSummary.jobSeq,
                    jLog.logResult,
                    jLog.logState,
                    jLog.logTime))
                .from(jLog)
                .where(jLog.jobSummary.jobSeq.eq(jobSeq))
                .orderBy(jLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }
}