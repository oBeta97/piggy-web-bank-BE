package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeaturesRepository extends JpaRepository<Feature, Long> {

    Optional<Feature> findByName(String name);

}
