package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.interfaces.UsersEntity;
import oBeta.PiggyWebBank.payloads.admin.FixedTransactionDTO;
import oBeta.PiggyWebBank.payloads.me.MeFixedTransactionDTO;

import java.util.UUID;

@Entity
@Table(name = "fixed_transactions")
@Setter
@Getter
@NoArgsConstructor
public class FixedTransaction extends Transaction implements UsersEntity {

    @Column(nullable = false)
    private short period;

    public FixedTransaction(String name, double amount, short period, User user) {
        super(name, amount, user);
        this.period = period;
    }

    public FixedTransaction(FixedTransactionDTO dto, User user){
        super(dto.name(), dto.amount(), user);
        this.period = dto.period();
    }

    public FixedTransaction(MeFixedTransactionDTO dto, User user){
        super(dto.name(), dto.amount(), user);
        this.period = dto.period();
    }
}
