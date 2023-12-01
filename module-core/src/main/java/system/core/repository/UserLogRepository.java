package system.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.core.entity.UserLog;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    // ...
}