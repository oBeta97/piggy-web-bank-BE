package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.VariableTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<VariableTransaction> findAllByUser(User user, Pageable pageable);

    List<VariableTransaction> findAllByUser(User user);

    @Query("SELECT vt FROM VariableTransaction vt WHERE vt.user = :user AND MONTH(vt.transactionDt) = MONTH(CURRENT_DATE) AND YEAR(vt.transactionDt) = YEAR(CURRENT_DATE)")
    List<VariableTransaction> findAllOfThisMonth(User user);


}
