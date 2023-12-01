package system.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.query.UserLogQuery;
import system.api.payload.query.UserQuery;
import system.api.payload.request.UserSearchRequest;
import system.core.entity.User;
import system.core.exception.UserException;
import system.core.exception.UserLogException;
import system.core.repository.query.UserLogQueryRepository;
import system.core.repository.query.UserQueryRepository;
import system.share.base.utility.JpaUtils;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserQueryRepository userQueryRepository;
    private final UserLogQueryRepository userLogQueryRepository;

    /**
     * User.userCode 값의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsUserCode(String userCode) {
        return userQueryRepository.existsByUserCode(userCode);
    }

    /**
     * User.userEmail 값의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean existsUserEmail(String userEmail) {
        return userQueryRepository.existsByUserEmail(userEmail);
    }

    /**
     * User 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public User getUser(Long userSeq) {
        return userQueryRepository
                .getByUserSeq(userSeq)
                .orElseThrow(() -> UserException.userNotFound());
    }

    /**
     * User 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public User getUser(String userCode) {
        return userQueryRepository
                .getByUserCode(userCode)
                .orElseThrow(() -> UserException.userNotFound());
    }

    /**
     * User 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<UserQuery> getUserList(UserSearchRequest request, Pageable pageable) {
        String sort = request.getSort().name();
        String sortBy = request.getSortBy().name();
        pageable = JpaUtils.set(pageable, sort, sortBy);

        return userQueryRepository.getUserList(request.getKeyword(), request.getSortBy(), pageable);
    }

    /**
     * UserLog 객체를 조회한다.
     */
    @Transactional(readOnly = true)
    public UserLogQuery getUserLog(Long logSeq) {
        return userLogQueryRepository
                .getUserLog(logSeq)
                .orElseThrow(() -> UserLogException.userLogNotFound());
    }

    /**
     * UserLog 컬렉션의 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Long getUserLogCount() {
        return userLogQueryRepository.getUserLogCount();
    }

    /**
     * UserLog 컬렉션의 상위 10개를 조회한다.
     */
    @Transactional(readOnly = true)
    public List<UserLogQuery> getUserLogList() {
        return userLogQueryRepository.getUserLogList();
    }

    /**
     * UserLog 컬렉션을 조회한다.
     */
    @Transactional(readOnly = true)
    public Page<UserLogQuery> getUserLogList(Pageable pageable) {
        pageable = JpaUtils.set(pageable);

        return userLogQueryRepository.getUserLogList(pageable);
    }
}