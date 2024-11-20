package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fixed_transactions")
@Setter
@Getter
@NoArgsConstructor
public class FixedTransaction extends Transaction{

    @Column(nullable = false)
    private short period;

    public FixedTransaction(String name, Double amount, short period) {
        super(name, amount);
        this.period = period;
    }
}
