package oBeta.PiggyWebBank.builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import oBeta.PiggyWebBank.entities.*;
import oBeta.PiggyWebBank.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class MonthHistoryBuilder {

    @Setter
    private long month;
    @Setter
    private long year;
    private Double available;
    private Double earnings;
    private Double expenses;
    private Double savings;
    private Double minimumSavings;
    private Double totSavings;
    @Setter
    private User user;

    @Autowired
    @Getter(AccessLevel.NONE)
    private UserService userService;

    @Autowired
    @Getter(AccessLevel.NONE)
    private FixedTransactionsService fixedTransactionsService;

    @Autowired
    @Getter(AccessLevel.NONE)
    private VariableTransactionsService variableTransactionsService;

    @Autowired
    @Getter(AccessLevel.NONE)
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    @Getter(AccessLevel.NONE)
    private GoalsService goalsService;

    private void setMinimumSavings (){
        this.minimumSavings = this.userCharacteristicsService.
                getUserCharacteristicByUser(this.user).
                getMinimumSavings();
    }

    private void setAvailable(){
        List<FixedTransaction> fixedTransactionList = this.fixedTransactionsService.getAllUserFixedTransactions(this.user);
        List<Goal> goalList = this.goalsService.getGoalsByUser(this.user);

        Double availableCash = fixedTransactionList.stream().mapToDouble(Transaction::getAmount).sum();
        Double availableFromGoals = goalList.stream().mapToDouble(Goal::getInstallment).sum();

        this.available = availableCash + availableFromGoals - this.minimumSavings;
        this.userCharacteristicsService.updateUserCharacteristicDailyAmount(this.user, this.available);
    }

    private void setEarnings(){
        this.earnings = this.variableTransactionsService.
                getVariableEarningsByUser(this.user).
                stream().
                mapToDouble(Transaction::getAmount).
                sum();
    }

    private void setExpenses(){
        this.expenses = this.variableTransactionsService.
                getVariableExpensesByUSer(this.user).
                stream().
                mapToDouble(Transaction::getAmount).
                sum();
    }

    private void setSavings(){
        this.savings = this.available + this.earnings + this.expenses;
    }

    private void setTotSavings(){
        this.totSavings = this.savings + this.minimumSavings;
    }

    public MonthHistory buildNewMonth (long year, long month, User user){

        // check if the data of the user are right
        User checkedUser = this.userService.getUserById(user.getId());

        this.setYear(year);
        this.setMonth(month);
        this.setUser(checkedUser);
        this.setMinimumSavings();
        this.setAvailable();
        this.setEarnings();
        this.setExpenses();
        this.setSavings();
        this.setTotSavings();

        return new MonthHistory(this);

    }

}
