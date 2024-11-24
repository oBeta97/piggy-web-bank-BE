package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCharacteristicsRepository extends JpaRepository<UserCharacteristic, Long> {

    Optional<UserCharacteristic> findByUser(User user);
}
