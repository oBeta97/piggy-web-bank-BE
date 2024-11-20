package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "transaction_categories")
@Data
@NoArgsConstructor
public class TransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "is_expense", nullable = false)
    private Boolean isExpense;

    public TransactionCategory(Long name, User user, Boolean isExpense) {
        this.name = name;
        this.user = user;
        this.isExpense = isExpense;
    }
}


