package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTransactionsRepository extends JpaRepository<FixedTransaction, Long> {
}
