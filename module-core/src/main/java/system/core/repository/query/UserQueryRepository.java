package system.core.repository.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.QUserQuery;
import system.api.payload.query.UserQuery;
import system.core.entity.QUser;
import system.core.entity.User;
import system.core.enums.search.UserSortBy;
import system.share.base.utility.JpaUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory query;
    private final QUser user = QUser.user;

    /**
     * User.userCode 값의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsByUserCode(String userCode) {
        Integer result = query.selectOne()
                .from(user)
                .where(user.userCode.eq(userCode))
                .fetchFirst();

        return result != null;
    }

    /**
     * User.userEmail 값의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsByUserEmail(String userEmail) {
        Integer result = query.selectOne()
                .from(user)
                .where(user.userEmail.eq(userEmail))
                .fetchFirst();

        return result != null;
    }

    /**
     * User 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<UserQuery> getUserList(String keyword, UserSortBy sortBy, Pageable pageable) {
        Long total = getCountByUserSearch(keyword, sortBy);

        List<UserQuery> results = query.select(new QUserQuery(
                    user.userSeq,
                    user.userCode,
                    user.userName,
                    user.userEmail,
                    user.gradeType,
                    user.storeType))
                .from(user)
                .where(sortBy(keyword, sortBy))
                .orderBy(orderBy(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return JpaUtils.toPage(results, total, pageable);
    }

    /**
     * User 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<User> getByUserSeq(Long userSeq) {
        return Optional.ofNullable(
                query.selectFrom(user)
                     .where(user.userSeq.eq(userSeq))
                     .fetchOne());
    }

    /**
     * User 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<User> getByUserCode(String userCode) {
        return Optional.ofNullable(
                query.selectFrom(user)
                     .where(user.userCode.eq(userCode))
                     .fetchOne());
    }

    /**
     * User 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<User> getByUserEmail(String userEmail) {
        return Optional.ofNullable(
                query.selectFrom(user)
                     .where(user.userEmail.eq(userEmail))
                     .fetchOne());
    }

    /**
     * User 컬렉션의 개수를 조회한다.
     */
    private Long getCountByUserSearch(String keyword, UserSortBy sortBy) {
        return query.select(user.count())
                .from(user)
                .where(sortBy(keyword, sortBy))
                .fetchOne();
    }

    /**
     * 검색 및 정렬 기준 조건을 적용한다.
     * <p> : UserSortByType.SEQ 조건에서 String 타입의 keyword 타입 변환 시, 오류 발생
     */
    private BooleanExpression sortBy(String keyword, UserSortBy sortBy) {
        if (StringUtils.isEmpty(keyword)) return null;

        return switch (sortBy) {
            case SEQ -> user.userSeq.eq(Long.valueOf(keyword));
            case NAME -> user.userName.eq(keyword);
            case CODE -> user.userCode.eq(keyword);
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
                    return new OrderSpecifier<>(sort, user.userSeq);
                case "NAME":
                    return new OrderSpecifier<>(sort, user.userName);
                case "CODE":
                    return new OrderSpecifier<>(sort, user.userCode);
            }
        }

        return null;
    }
}