package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.MonthHistory;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonthHistoriesRepository extends JpaRepository<MonthHistory, Long> {

    Optional<MonthHistory> findByMonthAndYearAndUser(long month, long year, User user);

    @Query("SELECT mh " +
            "FROM MonthHistory mh " +
            "WHERE " +
                "mh.User = :user " +
                "AND (" +
                    "mh.year < :year OR mh.year = :year AND mh.month < :month" +
                ")"
    )
    List<MonthHistory> findAllBeforeDate(long year, long month, User user);

    @Query("DELETE FROM MonthHistory mh WHERE mh.id IN :ids")
    int deleteByIds(List<Long> ids);


}
