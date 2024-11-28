package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalsRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g " +
            "FROM Goal g " +
            "WHERE g.experityDt >= :experityDt " +
            "AND g.user = :user")
    List<Goal> findByUserNotExpired(User user, LocalDate experityDt);

    @Query("SELECT g " +
            "FROM Goal g " +
            "WHERE g.experityDt >= :experityDt " +
            "AND g.user = :user")
    Page<Goal> findByUserNotExpired(User user, LocalDate experityDt, Pageable pageable);

    Optional<Goal> findByIdAndUser(long id, User user);


    List<Goal> findByUser(User user);

    Page<Goal> findByUser(User user, Pageable pageable);


}
