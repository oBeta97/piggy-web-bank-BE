package oBeta.PiggyWebBank.controllers.me;

import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.admin.GoalDTO;
import oBeta.PiggyWebBank.payloads.me.MeGoalDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/goals")
public class MeGoalsController {

    @Autowired
    private GoalsService goalsService;

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @GetMapping
    public Page<Goal> getAllMeGoalsNotExpired (
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean all
    ){
        if(all)
            return this.goalsService.getAllGoalsByUser(page, size, sortBy, loggedUser);

        return this.goalsService.getAllGoalsNotExpiredByUser(page,size,sortBy,loggedUser);
    }


    @GetMapping("/{goalId}")
    public Goal getMeGoalById(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long goalId
    ){
        return this.goalsService.getGoalByIdAndUser(goalId, loggedUser);
    }

    @PostMapping
    public Goal addMeGoal(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeGoalDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.goalsService.saveNewGoal(
                new GoalDTO(body.name(), body.amount(), body.expirityDt(), loggedUser.getId().toString())
        );
    }

    @PutMapping("/{goalId}")
    public Goal updateMeGoal(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long goalId,
            @RequestBody @Validated MeGoalDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        Goal g = this.goalsService.getGoalById(goalId);

        this.userValidation.validateUser(loggedUser, g, Goal.class, goalId);

        return this.goalsService.updateGoal(
                goalId,
                new GoalDTO(body.name(), body.amount(), body.expirityDt(), loggedUser.getId().toString())
        );
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeGoal(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long goalId
    ){

        Goal g = this.goalsService.getGoalById(goalId);

        this.userValidation.validateUser(loggedUser, g, Goal.class, goalId);

        this.goalsService.deleteGoal(goalId);

    }


}
