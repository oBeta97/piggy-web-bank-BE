package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.MonthHistory;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/month-histories")
public class MeMonthHistoriesController {

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @GetMapping("/last")
    public MonthHistory getLastMeMonthHistory (
            @AuthenticationPrincipal User loggedUser
    ){
        return this.monthHistoriesService.getLastMonthHistoryByUser(loggedUser);
    }

    @GetMapping
    public Page<MonthHistory> getAllMeMonthHistory(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.monthHistoriesService.getAllMeMonthHistory(page, size, sortBy, loggedUser);
    }

    @GetMapping("/{monthHistoryId}")
    public MonthHistory getMeMonthHistoryById (
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long monthHistoryId
    ){
        return this.monthHistoriesService.getMonthHistoryByIdAndUser(monthHistoryId, loggedUser);
    }

}
