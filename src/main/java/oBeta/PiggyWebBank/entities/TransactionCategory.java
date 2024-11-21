package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.payloads.BaseTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.TransactionCategoryDTO;

import java.util.UUID;

@Entity
@Table(name = "transaction_categories")
@Data
@NoArgsConstructor
public class TransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "is_expense", nullable = false)
    private Boolean isExpense;

    public TransactionCategory(String name, User user, Boolean isExpense) {
        this.name = name;
        this.user = user;
        this.isExpense = isExpense;
    }

    public TransactionCategory(BaseTransactionCategoryDTO dto){
        this.name = dto.name();
        this.user = null;
        this.isExpense = dto.isExpense();
    }

    public TransactionCategory(TransactionCategoryDTO dto, User user){
        this.name = dto.name();
        this.user = user;
        this.isExpense = dto.isExpense();
    }
}


