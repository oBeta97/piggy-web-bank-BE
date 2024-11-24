package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.payloads.GoalDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "goal_dt", nullable = false)
    private LocalDate goalDt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private double amount;

    @Column(nullable = false)
    private double installment;

    @Column(name = "experity_dt", nullable = false)
    private LocalDate experityDt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Goal(LocalDate goalDt, String name, double amount, double installment, LocalDate experityDt, User user) {
        this.goalDt = goalDt;
        this.name = name;
        this.amount = amount;
        this.installment = installment;
        this.experityDt = experityDt;
        this.user = user;
    }

    public Goal(GoalDTO dto, User user){
        this.goalDt = LocalDate.now();
        this.name = dto.name();
        this.experityDt = dto.expirityDt();
        setAmount(dto.amount());
        this.user = user;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.installment = amount / ChronoUnit.MONTHS.between(LocalDate.now(), this.experityDt);
    }
}