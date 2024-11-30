package oBeta.PiggyWebBank.schedulers;

import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserCharacteristicsScheduler {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Scheduled(cron = "0 30 0 * * ?")
    public void todayAmountUpdate(){
        try{
            this.userCharacteristicsService.updateAllTodayAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
