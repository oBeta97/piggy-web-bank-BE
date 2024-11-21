package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCharacteristcsRepository extends JpaRepository<UserCharacteristic, Long> {

    Optional<UserCharacteristic> findByUser(User user);
}
