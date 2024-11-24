package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);

}
