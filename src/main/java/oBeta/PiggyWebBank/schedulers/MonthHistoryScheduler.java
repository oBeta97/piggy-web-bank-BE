package oBeta.PiggyWebBank.schedulers;


import oBeta.PiggyWebBank.builders.MonthHistoryBuilder;
import oBeta.PiggyWebBank.entities.MonthHistory;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MonthHistoryScheduler {

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 30 0 1 * ?")
    public void monthHistoryUpdate(){

        LocalDate actualDate = LocalDate.now();

        List<User> users = this.userService.getAllUsers();

        try{
            for (User u : users){
                this.monthHistoriesService.saveNewMonthHistory(u);
                this.monthHistoriesService.reloadLastMonthHistoty(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
