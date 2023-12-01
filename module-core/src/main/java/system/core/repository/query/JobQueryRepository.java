package system.core.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.JobQuery;
import system.api.payload.query.QJobQuery;
import system.core.entity.schedule.JobSummary;
import system.core.entity.schedule.QJobSummary;
import system.core.enums.schedule.ScheduleStateType;
import system.core.enums.search.JobSortBy;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobQueryRepository {
    private final JPAQueryFactory query;
    private final QJobSummary jSummary = QJobSummary.jobSummary;

    /**
     * JobSummary 객체의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsJobSummary(JobKey jKey) {
        Integer result = query.selectOne()
                .from(jSummary)
                .where(jSummary.jobName.eq(jKey.getName()),
                       jSummary.jobGroup.eq(jKey.getGroup()))
                .fetchFirst();

        return result != null;
    }

    @Transactional(readOnly = true)
    public Boolean equalsJobRegister(JobKey jKey, String jRequester) {
        Integer result = query.selectOne()
                .from(jSummary)
                .where(jSummary.jobName.eq(jKey.getName()),
                       jSummary.jobGroup.eq(jKey.getGroup()),
                       jSummary.jobRegister.eq(jRequester))
                .fetchFirst();

        return result != null;
    }

    /**
     * JobSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<JobSummary> getJobSummary(JobKey jKey) {
        return Optional.ofNullable(
                query.selectFrom(jSummary)
                     .where(jSummary.jobName.eq(jKey.getName()),
                            jSummary.jobGroup.eq(jKey.getGroup()))
                     .fetchOne());
    }

    /**
     * JobSummary 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<JobQuery> getJobSummary(Long jSeq) {
        return Optional.ofNullable(
                query.select(new QJobQuery(
                        jSummary.jobSeq,
                        jSummary.jobName,
                        jSummary.jobGroup,
                        jSummary.jobRegister,
                        jSummary.classType,
                        jSummary.className,
                        jSummary.classData,
                        jSummary.classComment,
                        jSummary.stateType,
                        jSummary.storeType))
                     .from(jSummary)
                     .where(jSummary.jobSeq.eq(jSeq))
                     .fetchOne());
    }

    @Transactional(readOnly = true)
    public Long getJobSummaryCount(ScheduleStateType... stateTypes) {
        return query.select(jSummary.count())
                .from(jSummary)
                .where(equalsStateType(stateTypes))
                .fetchOne();
    }

    /**
     * JobSummary 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<JobQuery> getJobSummaryList(String keyword, JobSortBy sortBy, Pageable pageable) {
        Long total = query.select(jSummary.count())
                .from(jSummary)
                .fetchOne();

        List<JobQuery> results = query.select(new QJobQuery(
                    jSummary.jobSeq,
                    jSummary.jobName,
                    jSummary.jobGroup,
                    jSummary.jobRegister,
                    jSummary.stateType,
                    jSummary.storeType))
                .from(jSummary)
                .where(sortBy(keyword, sortBy))
                .orderBy(orderBy(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    private BooleanBuilder equalsStateType(ScheduleStateType... stateTypes) {
        BooleanBuilder builder = new BooleanBuilder();

        for (ScheduleStateType type : stateTypes) {
            builder.or(jSummary.stateType.eq(type));
        }

        return builder;
    }

    /**
     * 검색 및 정렬 기준 조건을 적용한다.
     * <p> : JobSortByType.SEQ 조건에서 String 타입의 keyword 타입 변환 시, 오류 발생
     */
    private BooleanExpression sortBy(String keyword, JobSortBy sortBy) {
        if (StringUtils.isEmpty(keyword)) return null;

        return switch (sortBy) {
            case SEQ -> jSummary.jobSeq.eq(Long.valueOf(keyword));
            case NAME -> jSummary.jobName.eq(keyword);
            case GROUP -> jSummary.jobGroup.eq(keyword);
            case REGISTER -> jSummary.jobRegister.eq(keyword);
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
                    return new OrderSpecifier<>(sort, jSummary.jobSeq);
                case "NAME":
                    return new OrderSpecifier<>(sort, jSummary.jobName);
                case "GROUP":
                    return new OrderSpecifier<>(sort, jSummary.jobGroup);
                case "REGISTER":
                    return new OrderSpecifier<>(sort, jSummary.jobRegister);
            }
        }

        return null;
    }
}