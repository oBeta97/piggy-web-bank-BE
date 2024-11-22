package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.MonthHistory;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/month-histories")
public class MonthHistoryController {

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @GetMapping
    public Page<MonthHistory> getAllMonthHistoriess(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.monthHistoriesService.getAllMonthHistory(page, size, sortBy);
    }

    @GetMapping("/{featureId}")
    public MonthHistory getMonthHistoryById(@PathVariable long featureId){
        return this.monthHistoriesService.getMonthHistoryById(featureId);
    }

}
