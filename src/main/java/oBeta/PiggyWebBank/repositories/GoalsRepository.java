package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalsRepository extends JpaRepository<Goal, Long> {
}
