package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedTransactionsRepository extends JpaRepository<FixedTransaction, Long> {

    List<FixedTransaction> findByUser(User user);

    @Query("SELECT ft FROM FixedTransaction ft WHERE ft.amount > 0 AND ft.user = :user")
    List<FixedTransaction> findFixedEarningByUser(User user);


    @Query("SELECT ft FROM FixedTransaction ft WHERE ft.amount < 0 AND ft.user = :user")
    List<FixedTransaction> findFixedExpensesByUser(User user);
}
