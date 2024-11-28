package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedTransactionsRepository extends JpaRepository<FixedTransaction, Long> {

    List<FixedTransaction> findByUser(User user);

    Page<FixedTransaction> findByUser(User user, Pageable pageable);

    Optional<FixedTransaction> findByIdAndUser(long id, User user);

    @Query("SELECT ft FROM FixedTransaction ft WHERE ft.amount > 0 AND ft.user = :user")
    List<FixedTransaction> findFixedEarningByUser(User user);


    @Query("SELECT ft FROM FixedTransaction ft WHERE ft.amount < 0 AND ft.user = :user")
    List<FixedTransaction> findFixedExpensesByUser(User user);
}
