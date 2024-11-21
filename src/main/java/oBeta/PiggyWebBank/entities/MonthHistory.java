package oBeta.PiggyWebBank.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "month_history")
@Data
@NoArgsConstructor
public class MonthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long month;

    @Column(nullable = false)
    private long year;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Double available;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Double earnings;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Double expenses;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Double savings;

    @Column(name = "minimum_savings", nullable = false)
    private Double minimumSavings;

    @Column(name = "tot_savings", nullable = false)
    private Double totSavings;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public MonthHistory(long month, long year, Double available, Double earnings, Double expenses, Double minimumSavings, Double totSavings, User user) {
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
