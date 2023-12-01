package system.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.core.entity.schedule.TriggerLog;

@Repository
public interface TriggerLogRepository extends JpaRepository<TriggerLog, Long> {
    // ...
}