package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionCategoriesRepository extends JpaRepository<TransactionCategory, Long> {
}
