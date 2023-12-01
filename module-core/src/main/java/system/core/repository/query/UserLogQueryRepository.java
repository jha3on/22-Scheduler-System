package system.core.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.QUserLogQuery;
import system.api.payload.query.UserLogQuery;
import system.core.entity.QUserLog;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserLogQueryRepository {
    private final JPAQueryFactory query;
    private final QUserLog userLog = QUserLog.userLog;

    /**
     * UserLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<UserLogQuery> getUserLog(Long logSeq) {
        return Optional.ofNullable(
                query.select(new QUserLogQuery(
                        userLog.logSeq,
                        userLog.jobSummary.jobSeq,
                        userLog.triggerSummary.triggerSeq,
                        userLog.logResult,
                        userLog.logDetail,
                        userLog.user.userCode,
                        userLog.logType,
                        userLog.logTime))
                     .from(userLog)
                     .where(userLog.logSeq.eq(logSeq))
                     .fetchOne());
    }

    /**
     * UserLog 컬렉션의 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Long getUserLogCount() {
        return query.select(userLog.count())
                .from(userLog)
                .fetchOne();
    }

    /**
     * UserLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<UserLogQuery> getUserLogList() {
        return query.select(new QUserLogQuery(
                    userLog.logSeq,
                    userLog.jobSummary.jobSeq,
                    userLog.triggerSummary.triggerSeq,
                    userLog.logResult,
                    userLog.user.userCode,
                    userLog.logType,
                    userLog.logTime))
                .from(userLog)
                .orderBy(userLog.logSeq.desc())
                .limit(10)
                .fetch();
    }

    /**
     * UserLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<UserLogQuery> getUserLogList(Pageable pageable) {
        Long total = query.select(userLog.count())
                .from(userLog)
                .fetchOne();

        List<UserLogQuery> results = query.select(new QUserLogQuery(
                    userLog.logSeq,
                    userLog.jobSummary.jobSeq,
                    userLog.triggerSummary.triggerSeq,
                    userLog.logResult,
                    userLog.user.userCode,
                    userLog.logType,
                    userLog.logTime))
                .from(userLog)
                .orderBy(userLog.logSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }
}