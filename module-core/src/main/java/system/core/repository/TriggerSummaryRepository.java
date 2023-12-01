package system.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.core.entity.schedule.TriggerSummary;

@Repository
public interface TriggerSummaryRepository extends JpaRepository<TriggerSummary, Long> {
    // ...
}