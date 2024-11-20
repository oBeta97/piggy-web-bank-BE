package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.MonthHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthHistoriesRepository extends JpaRepository<MonthHistory, Long> {
}
