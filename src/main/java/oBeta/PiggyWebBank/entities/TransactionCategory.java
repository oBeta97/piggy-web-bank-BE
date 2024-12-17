package oBeta.PiggyWebBank.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.interfaces.UsersEntity;
import oBeta.PiggyWebBank.payloads.admin.BaseTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.admin.UserTransactionCategoryDTO;

@Entity
@Table(name = "transaction_categories")
@Data
@NoArgsConstructor
public class TransactionCategory implements UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
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

    public TransactionCategory(UserTransactionCategoryDTO dto, User user){
        this.name = dto.name();
        this.user = user;
        this.isExpense = dto.isExpense();
    }
}


