package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oBeta.PiggyWebBank.builders.MonthHistoryBuilder;

@Entity
@Table(name = "month_history")
@Data
@NoArgsConstructor
public class MonthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long month;

    @Column(nullable = false)
    private long year;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private double available;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private double earnings;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private double expenses;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private double savings;

    @Column(name = "minimum_savings", nullable = false)
    private double minimumSavings;

    @Column(name = "tot_savings", nullable = false)
    private double totSavings;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public MonthHistory(long month, long year, double available, double earnings, double expenses, double minimumSavings, double totSavings, User user) {
        this.month = month;
        this.year = year;
        this.available = available;
        this.earnings = earnings;
        this.expenses = expenses;
        this.savings = available + earnings + expenses;
        this.minimumSavings = minimumSavings;
        this.totSavings = totSavings;
        this.user = user;
    }

    public MonthHistory(long month, long year, User user) {
        this.month = month;
        this.year = year;
        this.available = 0.0;
        this.earnings = 0.0;
        this.expenses = 0.0;
        this.savings = 0.0;
        this.minimumSavings = 0.0;
        this.totSavings = 0.0;
        this.user = user;
    }

    public MonthHistory(MonthHistoryBuilder builder){
        this.month = builder.getMonth();
        this.year = builder.getYear();
        this.available = builder.getAvailable();
        this.earnings = builder.getEarnings();
        this.expenses = builder.getExpenses();
        this.savings = builder.getSavings();
        this.minimumSavings = builder.getMinimumSavings();
        this.totSavings = builder.getTotSavings();
        this.user = builder.getUser();
    }

    public void updateSavings(){
        this.savings = this.getAvailable() + this.getEarnings() + this.getExpenses();
    }

    public void setAvailable(double available){
        this.available = available;
        this.updateSavings();
    }

    public void setEarnings(double earnings){
        this.earnings = earnings;
        this.updateSavings();
    }

    public void setExpenses(double expenses){
        this.expenses = expenses;
        this.updateSavings();
    }

}
