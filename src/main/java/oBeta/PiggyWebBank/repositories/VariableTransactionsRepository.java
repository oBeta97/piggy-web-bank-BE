package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.VariableTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariableTransactionsRepository extends JpaRepository<VariableTransaction, Long> {

    @Query("SELECT vt FROM VariableTransaction vt WHERE vt.amount < 0 AND vt.user = :user AND vt.transactionDt >= DATE_TRUNC('month', CURRENT_DATE) ")
    List<VariableTransaction> findVariableExpensesByUserOfThisMonth(User user);

    @Query("SELECT vt FROM VariableTransaction vt WHERE vt.amount > 0 AND vt.user = :user AND vt.transactionDt >= DATE_TRUNC('month', CURRENT_DATE) ")
    List<VariableTransaction> findVariableEarningsByUserOfThisMonth(User user);

}
