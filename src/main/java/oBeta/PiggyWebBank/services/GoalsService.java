package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.entities.User;
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

@Service
public class GoalsService {

    @Autowired
    private GoalsRepository goalsRepo;

    @Autowired
    private UserService userService;

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
        User user = this.userService.getUserById(dto.user_id());

        return this.goalsRepo.save(
                new Goal(dto, user)
        );
    }

    public Goal updateGoal(long idToUpdate, GoalDTO dto){

        Goal found = this.getGoalById(idToUpdate);

        found.setName(dto.name());
        found.setPeriod(dto.period());
        found.setAmount(dto.amount());

        return this.goalsRepo.save(found);
    }

    public void deleteGoal(long idToDelete){
        this.goalsRepo.delete(
            this.getGoalById(idToDelete)
        );
    }

}
