package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.builders.MonthHistoryBuilder;
import oBeta.PiggyWebBank.entities.MonthHistory;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.repositories.MonthHistoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MonthHistoriesService {

    @Autowired
    private MonthHistoriesRepository monthHistoriesRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoryBuilder monthHistoryBuilder;

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    public Page<MonthHistory> getAllMonthHistory(int page, int size, String sortBy) {
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.monthHistoriesRepo.findAll(pageable);
    }

    public Page<MonthHistory> getAllMeMonthHistory(int page, int size, String sortBy, User user){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.monthHistoriesRepo.findByUser(user, pageable);
    }

    public MonthHistory getMonthHistoryById (long idToFind){
        return this.monthHistoriesRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("Month History with id " + idToFind + " not found!")
                );
    }

    public MonthHistory getMonthHistoryByIdAndUser (long idToFind, User user){
        return this.monthHistoriesRepo.findByIdAndUser(idToFind, user)
                .orElseThrow(() ->
                        new NotFoundException("Month History with id " + idToFind + " not found!")
                );
    }

    // Month History is a derived table. It can't be changed by the user!
    public MonthHistory saveNewMonthHistory(User user){

        LocalDate actualDate = LocalDate.now();

        this.monthHistoriesRepo.findByMonthAndYearAndUser(actualDate.getMonthValue(), actualDate.getYear(), user)
            .ifPresent(feature -> {
                    throw new BadRequestException("MonthHistory of this month of the user " + user.getUsername() + " already exist!");
            });

        return this.monthHistoriesRepo.save(new MonthHistory(actualDate.getMonthValue(), actualDate.getYear(), user));
    }

    public MonthHistory getLastMonthHistoryByUser (User user){
        return this.monthHistoriesRepo.findFirstByUserOrderByIdDesc(user);
    }

    public void reloadLastMonthHistoty(User user){

        MonthHistory lastMonthHistory = this.getLastMonthHistoryByUser(user);

        // Cause of the flush of the save must be cloned the values
        double lastMonthHistoryAvailable = lastMonthHistory.getAvailable();
        double lastMonthHistorySavings = lastMonthHistory.getSavings();

        MonthHistory updatedMonth = this.monthHistoryBuilder.buildNewMonth(lastMonthHistory.getYear(), lastMonthHistory.getMonth(), lastMonthHistory.getUser());
        updatedMonth.setId(lastMonthHistory.getId());

        this.monthHistoriesRepo.save(updatedMonth);

        if(updatedMonth.getAvailable() != lastMonthHistoryAvailable)
            this.userCharacteristicsService.updateUserCharacteristicDailyAmount(user,updatedMonth.getAvailable());

        if(updatedMonth.getSavings() != lastMonthHistorySavings)
            this.userCharacteristicsService.updateUserCharacteristicTodayAmount(user, updatedMonth.getEarnings() + updatedMonth.getExpenses());
    }

    // DELETE of the older data than a specific date
    public void deleteMonthHistoryByDate (long year, long month, User user){

        List<MonthHistory> monthHistoryList = this.monthHistoriesRepo.findAllBeforeDate(year, month, user);

        this.monthHistoriesRepo.deleteByIds(
                monthHistoryList.stream()
                        .map(MonthHistory::getId)
                        .collect(Collectors.toList())
        );
    }


}
