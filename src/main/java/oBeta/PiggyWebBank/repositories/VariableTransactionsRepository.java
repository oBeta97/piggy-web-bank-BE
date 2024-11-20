package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.VariableTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableTransactionsRepository extends JpaRepository<VariableTransaction, Long> {
}
