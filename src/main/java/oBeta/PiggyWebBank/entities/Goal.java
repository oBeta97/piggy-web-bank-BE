package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.payloads.GoalDTO;

import java.time.LocalDate;

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
    private Short period;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double installment;

    @Column(name = "experity_dt", nullable = false)
    private LocalDate experityDt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Goal(LocalDate goalDt, String name, Short period, Double amount, Double installment, LocalDate experityDt, User user) {
        this.goalDt = goalDt;
        this.name = name;
        this.period = period;
        this.amount = amount;
        this.installment = installment;
        this.experityDt = experityDt;
        this.user = user;
    }

    public Goal(GoalDTO dto, User user){
        this.goalDt = LocalDate.now();
        this.name = dto.name();
        this.period = dto.period();
        this.amount = dto.amount();
        this.installment = dto.amount() / dto.period();
        this.experityDt = LocalDate.now().plusMonths(dto.period());
        this.user = user;
    }

    public void setPeriod(short period){
        this.period = period;
        this.experityDt = this.goalDt.plusMonths(period);
    }

}