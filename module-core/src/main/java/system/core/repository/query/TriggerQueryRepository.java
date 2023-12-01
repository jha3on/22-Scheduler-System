package system.core.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.QTriggerQuery;
import system.api.payload.query.TriggerQuery;
import system.core.entity.schedule.JobSummary;
import system.core.entity.schedule.QJobSummary;
import system.core.entity.schedule.QTriggerSummary;
import system.core.entity.schedule.TriggerSummary;
import system.core.enums.search.TriggerSortBy;
import system.share.base.utility.DateTimeUtils;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TriggerQueryRepository {
    private final JPAQueryFactory query;
    private final QJobSummary jSummary = QJobSummary.jobSummary;
    private final QTriggerSummary tSummary = QTriggerSummary.triggerSummary;

    /**
     * TriggerSummary 객체의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsTriggerSummary(JobKey jKey, TriggerKey tKey) {
        Integer result = query.selectOne()
                .from(tSummary)
                .where(tSummary.jobName.eq(jKey.getName()),
                       tSummary.jobGroup.eq(jKey.getGroup()),
                       tSummary.triggerName.eq(tKey.getName()),
                       tSummary.triggerGroup.eq(tKey.getGroup()))
                .fetchFirst();

        return result != null;
    }

    /**
     * TriggerSummary -> JobSummary.jobRegister 값을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean equalsJobRegister(TriggerKey tKey, String tRequester) {
        String jRegister = query.select(jSummary.jobRegister)
                .from(jSummary)
                .join(tSummary).on(equalsJobKey())
                .where(tSummary.triggerName.eq(tKey.getName()),
                       tSummary.triggerGroup.eq(tKey.getGroup()))
                .fetchOne();

        String tRegister = query.select(tSummary.triggerRegister)
                .from(tSummary)
                .where(tSummary.triggerName.eq(tKey.getName()),
                       tSummary.triggerGroup.eq(tKey.getGroup()))
                .fetchOne();

        if (StringUtils.isEmpty(jRegister)) return false;
        if (StringUtils.isEmpty(tRegister)) return false;

        if (jRegister.equals(tRequester)) return true;
        return tRegister.equals(tRequester);
    }

    /**
     * TriggerSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<TriggerSummary> getTriggerSummary(JobKey jKey, TriggerKey tKey) {
        return Optional.ofNullable(
                query.selectFrom(tSummary)
                     .where(tSummary.jobName.eq(jKey.getName()),
                            tSummary.jobGroup.eq(jKey.getGroup()),
                            tSummary.triggerName.eq(tKey.getName()),
                            tSummary.triggerGroup.eq(tKey.getGroup()))
                     .fetchOne());
    }

    /**
     * TriggerSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<TriggerQuery> getTriggerSummary(Long tSeq) {
        return Optional.ofNullable(
                query.select(new QTriggerQuery(
                        jSummary.jobSeq,
                        tSummary.triggerSeq,
                        tSummary.jobName,
                        tSummary.jobGroup,
                        tSummary.triggerName,
                        tSummary.triggerGroup,
                        tSummary.triggerRegister,
                        tSummary.classType,
                        tSummary.policyType,
                        tSummary.stateType,
                        tSummary.storeType,
                        tSummary.repeatCount.coalesce(-1).as("repeatCount"),
                        tSummary.repeatInterval.coalesce(-1).as("repeatInterval"),
                        tSummary.repeatExpression.coalesce("-1").as("repeatExpression"),
                        tSummary.prevFireTime.coalesce(DateTimeUtils.getMaxDateTime()).as("prevFireTime"),
                        tSummary.nextFireTime.coalesce(DateTimeUtils.getMaxDateTime()).as("nextFireTime"),
                        tSummary.startTime.coalesce(DateTimeUtils.getMaxDateTime()).as("startTime"),
                        tSummary.stopTime.coalesce(DateTimeUtils.getMaxDateTime()).as("stopTime")))
                     .from(tSummary)
                     .leftJoin(jSummary).on(equalsJobKey())
                     .where(tSummary.triggerSeq.eq(tSeq))
                     .fetchOne());
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<TriggerSummary> getTriggerSummaryList(JobKey jKey, List<TriggerKey> tKeys) {
        return query.selectFrom(tSummary)
                .where(tSummary.jobName.eq(jKey.getName()),
                       tSummary.jobGroup.eq(jKey.getGroup()),
                       equalsTriggerKeys(tKeys))
                .fetch();
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerQuery> getTriggerSummaryList(String keyword, TriggerSortBy sortBy, Pageable pageable) {
        Long total = query.select(tSummary.count())
                .from(tSummary)
                .where(sortBy(keyword, sortBy))
                .fetchOne();

        List<TriggerQuery> results = query.select(new QTriggerQuery(
                    jSummary.jobSeq,
                    tSummary.triggerSeq,
                    tSummary.jobName,
                    tSummary.jobGroup,
                    tSummary.triggerName,
                    tSummary.triggerGroup,
                    tSummary.triggerRegister,
                    tSummary.stateType,
                    tSummary.storeType))
                .from(tSummary)
                .leftJoin(jSummary).on(equalsJobKey())
                .where(sortBy(keyword, sortBy))
                .orderBy(orderBy(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerQuery> getTriggerSummaryList(Long jSeq, JobKey jKey, String keyword, TriggerSortBy sortBy, Pageable pageable) {
        return getTriggerSummariesQuery(jSeq, jKey, keyword, sortBy, pageable);
    }

    /**
     * TriggerSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<TriggerQuery> getTriggerSummaryList(Long jSeq, String keyword, TriggerSortBy sortBy, Pageable pageable) {
        JobKey jKey = getJobKey(Objects.requireNonNull(query.selectFrom(jSummary)
                .where(jSummary.jobSeq.eq(jSeq))
                .fetchOne()));

        return getTriggerSummariesQuery(jSeq, jKey, keyword, sortBy, pageable);
    }

    /**
     * JobKey 객체를 가져온다.
     */
    private JobKey getJobKey(JobSummary jSummary) {
        return JobKey.jobKey(jSummary.getJobName(), jSummary.getJobGroup());
    }

    /**
     * JobKey 객체의 일치 여부를 확인한다.
     */
    private BooleanBuilder equalsJobKey() {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(tSummary.jobName.eq(jSummary.jobName))
               .and(tSummary.jobGroup.eq(jSummary.jobGroup));

        return builder;
    }

    /**
     * JobKey 객체의 일치 여부를 확인한다.
     */
    private BooleanBuilder equalsJobKey(JobKey jKey) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(tSummary.jobName.eq(jKey.getName()))
               .and(tSummary.jobGroup.eq(jKey.getGroup()));

        return builder;
    }

    /**
     * TriggerKey 객체의 일치 여부를 확인한다.
     */
    private BooleanBuilder equalsTriggerKeys(List<TriggerKey> tKeys) {
        BooleanBuilder builder = new BooleanBuilder();

        if (tKeys.isEmpty()) return null;
        for (TriggerKey tKey : tKeys) {
            builder.or((tSummary.triggerName.eq(tKey.getName()).and(tSummary.triggerGroup.eq(tKey.getGroup()))));
        }

        return builder;
    }

    /**
     * 검색 및 정렬 기준 조건을 적용한 데이터를 가져온다.
     */
    private Page<TriggerQuery> getTriggerSummariesQuery(Long jSeq, JobKey jKey, String keyword, TriggerSortBy sortBy, Pageable pageable) {
        List<TriggerQuery> results = query.select(new QTriggerQuery(
                    Expressions.asNumber(jSeq),
                    tSummary.triggerSeq,
                    Expressions.asString(jKey.getName()),
                    Expressions.asString(jKey.getGroup()),
                    tSummary.triggerName,
                    tSummary.triggerGroup,
                    tSummary.triggerRegister,
                    tSummary.stateType,
                    tSummary.storeType))
                .from(tSummary)
                .where(tSummary.jobName.eq(jKey.getName()),
                       tSummary.jobGroup.eq(jKey.getGroup()))
                .where(sortBy(keyword, sortBy))
                .orderBy(orderBy(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, getPageSearchCount(jKey, keyword, sortBy), pageable);
    }

    /**
     * 검색 및 정렬 기준 조건을 적용한 데이터의 개수를 가져온다.
     */
    private Long getPageSearchCount(JobKey jKey, String keyword, TriggerSortBy sortBy) {
        return query.select(tSummary.count())
                .from(tSummary)
                .where(tSummary.jobName.eq(jKey.getName()),
                       tSummary.jobGroup.eq(jKey.getGroup()))
                .where(sortBy(keyword, sortBy))
                .fetchOne();
    }

    /**
     * 검색 및 정렬 기준 조건을 적용한다.
     * <p> : TriggerSortByType.SEQ 조건에서 String 타입의 keyword 타입 변환 시, 오류 발생
     */
    private BooleanExpression sortBy(String keyword, TriggerSortBy sortBy) {
        if (StringUtils.isEmpty(keyword)) return null;

        return switch (sortBy) {
            case SEQ -> tSummary.triggerSeq.eq(Long.valueOf(keyword));
            case NAME -> tSummary.jobName.eq(keyword);
            case GROUP -> tSummary.jobGroup.eq(keyword);
            case REGISTER -> tSummary.triggerRegister.eq(keyword);
        };
    }

    /**
     * 정렬 방향 조건을 적용한다.
     */
    private OrderSpecifier<?> orderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) return null;

        for (Sort.Order order : pageable.getSort()) {
            Order sort = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "SEQ":
                    return new OrderSpecifier<>(sort, tSummary.triggerSeq);
                case "NAME":
                    return new OrderSpecifier<>(sort, tSummary.jobName);
                case "GROUP":
                    return new OrderSpecifier<>(sort, tSummary.jobGroup);
                case "REGISTER":
                    return new OrderSpecifier<>(sort, tSummary.triggerRegister);
            }
        }

        return null;
    }
}