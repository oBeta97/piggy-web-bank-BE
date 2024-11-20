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
    private Double available;

    @Column(nullable = false)
    private Double earnings;

    @Column(nullable = false)
    private Double expenses;

    @Column(nullable = false)
    private Double savings;

    @Column(name = "minimum_savings", nullable = false)
    private Double minimumSavings;

    @Column(name = "tot_savings", nullable = false)
    private Double totSavings;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public MonthHistory(long month, long year, Double available, Double earnings, Double expenses, Double savings, Double minimumSavings, Double totSavings, User user) {
        this.month = month;
        this.year = year;
        this.available = available;
        this.earnings = earnings;
        this.expenses = expenses;
        this.savings = savings;
        this.minimumSavings = minimumSavings;
        this.totSavings = totSavings;
        this.user = user;
    }
}
