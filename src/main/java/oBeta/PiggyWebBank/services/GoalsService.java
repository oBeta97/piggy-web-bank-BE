package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.GoalDTO;
import oBeta.PiggyWebBank.repositories.GoalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GoalsService {

    @Autowired
    private GoalsRepository goalsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    public Page<Goal> getAllGoals (int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.goalsRepo.findAll(pageable);
    }

    public Goal getGoalById(long idToFind){
        return this.goalsRepo.findById(idToFind)
                .orElseThrow(() -> new NotFoundException("Goal with id " + idToFind + " not found!" ));
    }

    public List<Goal> getGoalsByUser(User user){
        return this.goalsRepo.findByUser(user);
    }

    public Goal saveNewGoal(GoalDTO dto){
        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }

        Goal res = this.goalsRepo.save(
                new Goal(dto, user)
        );

        this.monthHistoriesService.reloadLastMonthHistoty(user);

        return res;
    }

    public Goal updateGoal(long idToUpdate, GoalDTO dto){
        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }        Goal found = this.getGoalById(idToUpdate);

        found.setName(dto.name());
        found.setAmount(dto.amount());

        Goal res = this.goalsRepo.save(found);

        this.monthHistoriesService.reloadLastMonthHistoty(user);

        return res;
    }

    public void deleteGoal(long idToDelete){

        Goal goal = this.getGoalById(idToDelete);

        this.goalsRepo.delete(goal);

        this.monthHistoriesService.reloadLastMonthHistoty(goal.getUser());

    }

}
