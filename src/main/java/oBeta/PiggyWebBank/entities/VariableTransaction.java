package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.payloads.FixedTransactionDTO;
import oBeta.PiggyWebBank.payloads.VariableTransactionDTO;

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
    
    public VariableTransaction(String name, Double amount, LocalDateTime transactionDt, TransactionCategory transactionCategory, User user) {
        super(name, amount, user);
        this.transactionDt = transactionDt;
        this.transactionCategory = transactionCategory;
    }

    public VariableTransaction(VariableTransactionDTO dto, TransactionCategory transactionCategory, User user){
        super(dto.name(), dto.amount(), user);
        this.transactionDt = dto.transactionDt();
        this.transactionCategory = transactionCategory;
    }
}
