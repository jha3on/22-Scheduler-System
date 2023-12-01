package system.core.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.QTriggerLogQuery;
import system.api.payload.query.TriggerLogQuery;
import system.core.entity.schedule.QJobSummary;
import system.core.entity.schedule.QTriggerLog;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TriggerLogQueryRepository {
    private final JPAQueryFactory query;
    private final QJobSummary jSummary = QJobSummary.jobSummary;
    private final QTriggerLog tLog = QTriggerLog.triggerLog;

    /**
     * TriggerLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<TriggerLogQuery> getTriggerLog(Long logSeq) {
        return Optional.ofNullable(
                query.select(new QTriggerLogQuery(
                        tLog.logSeq,
                        jSummary.jobSeq,
                        tLog.triggerSummary.triggerSeq,
                        tLog.logResult,
                        tLog.logDetail,
                        tLog.logState,
                        tLog.logTime))
                     .from(tLog)
                     .leftJoin(jSummary).on(equalsJobKey())
                     .where(tLog.logSeq.eq(logSeq))
                     .fetchOne());
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogList(Pageable pageable) {
        Long total = query.select(tLog.count())
                .from(tLog)
                .fetchOne();

        List<TriggerLogQuery> results = query.select(new QTriggerLogQuery(
                    tLog.logSeq,
                    jSummary.jobSeq,
                    tLog.triggerSummary.triggerSeq,
                    tLog.logResult,
                    tLog.logState,
                    tLog.logTime))
                .from(tLog)
                .leftJoin(jSummary).on(equalsJobKey())
                .orderBy(tLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogListByJob(Long jobSeq, Pageable pageable) {
        Long total = query.select(tLog.count())
                .from(tLog)
                .innerJoin(jSummary).on(jSummary.jobSeq.eq(jobSeq).and(equalsJobKey()))
                .fetchOne();

        List<TriggerLogQuery> results = query.select(new QTriggerLogQuery(
                    tLog.logSeq,
                    Expressions.asNumber(jobSeq),
                    tLog.triggerSummary.triggerSeq,
                    tLog.logResult,
                    tLog.logState,
                    tLog.logTime))
                .from(tLog)
                .innerJoin(jSummary).on(jSummary.jobSeq.eq(jobSeq).and(equalsJobKey()))
                .orderBy(tLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * TriggerLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerLogQuery> getTriggerLogListByTrigger(Long triggerSeq, Pageable pageable) {
        Long total = query.select(tLog.count())
                .from(tLog)
                .where(tLog.triggerSummary.triggerSeq.eq(triggerSeq))
                .fetchOne();

        List<TriggerLogQuery> results = query.select(new QTriggerLogQuery(
                    tLog.logSeq,
                    jSummary.jobSeq,
                    tLog.triggerSummary.triggerSeq,
                    tLog.logResult,
                    tLog.logState,
                    tLog.logTime))
                .from(tLog)
                .where(tLog.triggerSummary.triggerSeq.eq(triggerSeq))
                .leftJoin(jSummary).on(equalsJobKey())
                .orderBy(tLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * JobKey 객체의 일치 여부를 확인한다.
     */
    private BooleanBuilder equalsJobKey() {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(tLog.triggerSummary.jobName.eq(jSummary.jobName))
               .and(tLog.triggerSummary.jobGroup.eq(jSummary.jobGroup));

        return builder;
    }
}