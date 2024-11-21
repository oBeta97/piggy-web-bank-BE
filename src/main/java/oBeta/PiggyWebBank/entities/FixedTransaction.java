package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.payloads.FixedTransactionDTO;

@Entity
@Table(name = "fixed_transactions")
@Setter
@Getter
@NoArgsConstructor
public class FixedTransaction extends Transaction{

    @Column(nullable = false)
    private short period;

    public FixedTransaction(String name, Double amount, short period, User user) {
        super(name, amount, user);
        this.period = period;
    }

    public FixedTransaction(FixedTransactionDTO dto, User user){
        super(dto.name(), dto.amount(), user);
        this.period = dto.period();
    }
}
