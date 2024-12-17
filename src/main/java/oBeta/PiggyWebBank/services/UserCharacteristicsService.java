package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.admin.UserCharacteristicDTO;
import oBeta.PiggyWebBank.repositories.UserCharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserCharacteristicsService {

    @Autowired
    private UserCharacteristicsRepository userCharacteristicsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    public Page<UserCharacteristic> getAllUserCharacteristics (int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userCharacteristicsRepo.findAll(pageable);
    }

    public UserCharacteristic getUserCharacteristicById (long idToFind){
        return this.userCharacteristicsRepo.findById(idToFind)
                .orElseThrow(() -> new NotFoundException("UserCharacteristic with id " + idToFind + " not found!" ));
    }

    public UserCharacteristic getUserCharacteristicByUser (User user){
        return this.userCharacteristicsRepo.findByUser(user)
                .orElseThrow(() -> new NotFoundException("UserCharacteristic of the user " + user.getUsername() + " not found!" ));
    }

    // This method will be used ONLY on signIn, so every data will be 0
    // and updated later with the triggers
    public UserCharacteristic saveNewUserCharacteristic (String currency, User user){
        return this.userCharacteristicsRepo.save(
          new UserCharacteristic(currency, user)
        );
    }

    public void updateMinimumSavings(double newMinimumSavings, User user){

        UserCharacteristic uc = user.getUserCharacteristic();

        uc.setMinimumSavings(newMinimumSavings);

        this.userCharacteristicsRepo.save(uc);
    }


    public UserCharacteristic updateUserCharacteristic(UserCharacteristicDTO dto){

        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }

        UserCharacteristic found = this.getUserCharacteristicByUser(user);

        double oldMinimumSaving = found.getMinimumSavings();

        found.setAvatar(dto.avatar());
        found.setCurrency(dto.currency());
        found.setMinimumSavings(dto.minimumSavings());

        UserCharacteristic res = this.userCharacteristicsRepo.save(found);

        if(oldMinimumSaving != dto.minimumSavings())
            this.monthHistoriesService.reloadLastMonthHistoty(user);

        return res;

    }

    public UserCharacteristic updateUserCharacteristicDailyAmount(User user, double available){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        found.setDailyAmount(available/ LocalDate.now().lengthOfMonth());

        return this.userCharacteristicsRepo.save(found);
    }

    public UserCharacteristic updateUserCharacteristicTodayAmount(User user, double summedTransaction){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        found.setTodayAmount(summedTransaction + found.getDailyAmount() * LocalDate.now().getDayOfMonth());

        return this.userCharacteristicsRepo.save(found);
    }

    public void updateAllTodayAmount(){
        this.userCharacteristicsRepo.updateAllTodayAmount();
    }


    // This method will be run ONLY when a user will be deleted!
    public void deleteUserCharacteristic (User user){
        User u = this.userService.getUserById(user.getId());

        UserCharacteristic found = this.getUserCharacteristicByUser(u);

        this.userCharacteristicsRepo.delete(found);
    }



}
