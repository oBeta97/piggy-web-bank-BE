package oBeta.PiggyWebBank.repositories;

import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionCategoriesRepository extends JpaRepository<TransactionCategory, Long> {

    @Query("SELECT tc FROM TransactionCategory tc WHERE tc.user IS NULL")
    List<TransactionCategory> findBaseTransactionCategory();

    List<TransactionCategory> findTransactionCategoryByUser(User user);

    @Query("SELECT tc FROM TransactionCategory tc WHERE tc.user IS NULL OR tc.user = :user  ")
    List<TransactionCategory> findAllTransactionCategoryOfAnUser(User user);
}
