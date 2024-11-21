package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.UserCharacteristicDTO;
import oBeta.PiggyWebBank.repositories.UserCharacteristcsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserCharacteristicsService {

    @Autowired
    private UserCharacteristcsRepository userCharacteristcsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    public Page<UserCharacteristic> getAllUserCharacteristics (int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userCharacteristcsRepo.findAll(pageable);
    }

    public UserCharacteristic getUserCharacteristicById (long idToFind){
        return this.userCharacteristcsRepo.findById(idToFind)
                .orElseThrow(() -> new NotFoundException("UserCharacteristic with id " + idToFind + " not found!" ));
    }

    public UserCharacteristic getUserCharacteristicByUser (User user){
        return this.userCharacteristcsRepo.findByUser(user)
                .orElseThrow(() -> new NotFoundException("UserCharacteristic of the user " + user.getUsername() + " not found!" ));
    }

    // This method will be used ONLY on signIn, so every data will be 0
    // and updated later with the triggers
    public UserCharacteristic saveNewUserCharacteristic (String currency, User user){
        return this.userCharacteristcsRepo.save(
          new UserCharacteristic(currency, user)
        );
    }

    public UserCharacteristic updateUserCharacteristic(UserCharacteristicDTO dto){

        User user = this.userService.getUserById(dto.user_id());

        UserCharacteristic found = this.getUserCharacteristicByUser(user);

        double oldMinimumSaving = found.getMinimumSavings();

        found.setAvatar(dto.avatar());
        found.setCurrency(dto.currency());
        found.setMinimumSavings(dto.minimumSavings());

        UserCharacteristic res = this.userCharacteristcsRepo.save(found);

        if(oldMinimumSaving != dto.minimumSavings())
            this.monthHistoriesService.reloadLastMonthHistoty(user);

        return res;

    }

    public UserCharacteristic updateUserCharacteristicDailyAmount(User user, double available){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        found.setDailyAmount(available/ LocalDate.now().lengthOfMonth());

        return this.userCharacteristcsRepo.save(found);
    }

    public UserCharacteristic updatUserCharacteristicTodayAmount(User user, double summedTransaction){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        found.setTodayAmount(summedTransaction + found.getDailyAmount() * LocalDate.now().getDayOfMonth());

        return this.userCharacteristcsRepo.save(found);

    }

    // This method will be run ONLY when a user will be deleted!
    public void deleteUserCharacteristic (User user){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        this.userCharacteristcsRepo.delete(found);
    }



}
