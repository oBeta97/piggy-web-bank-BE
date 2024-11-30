package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCharacteristicsRepository extends JpaRepository<UserCharacteristic, Long> {

    Optional<UserCharacteristic> findByUser(User user);

    @Modifying
    @Query("UPDATE uc SET uc.todayAmount = uc.todayAmount + uc.dailyAmount FROM UserCharacteristic uc")
    int updateAllTodayAmount();

}
