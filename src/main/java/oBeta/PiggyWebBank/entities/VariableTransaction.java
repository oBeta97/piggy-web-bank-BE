package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "variable_transactions")
@Setter
@Getter
@NoArgsConstructor
public class VariableTransaction extends Transaction{

    @Column(name = "transaction_dt", nullable = false)
    private LocalDateTime transactionDt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TransactionCategory transactionCategory;
    
    public VariableTransaction(String name, Double amount, LocalDateTime transactionDt, TransactionCategory transactionCategory) {
        super(name, amount);
        this.transactionDt = transactionDt;
        this.transactionCategory = transactionCategory;
    }
}
