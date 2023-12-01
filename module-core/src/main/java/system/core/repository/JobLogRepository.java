package system.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.core.entity.schedule.JobLog;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    // ...
}