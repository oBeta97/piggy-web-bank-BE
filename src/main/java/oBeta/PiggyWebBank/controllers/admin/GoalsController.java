package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.GoalDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/goals")
@PreAuthorize("hasAnyAuthority(" +
        "'goal:CRUD'," +
        "'goal:C'" +
        "'goal:R'," +
        "'goal:U'," +
        "'goal:D'," +
        ")"
)
public class GoalsController {

    @Autowired
    private GoalsService goalsService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('goal:CRUD', 'goal:R')")
    public Page<Goal> getAllGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.goalsService.getAllGoals(page, size, sortBy);
    }

    @GetMapping("/{goalId}")
    @PreAuthorize("hasAnyAuthority('goal:CRUD', 'goal:R')")
    public Goal getGoalById(@PathVariable long goalId){
        return this.goalsService.getGoalById(goalId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('goal:CRUD', 'goal:C')")
    public Goal saveNewGoal(@RequestBody @Validated GoalDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.goalsService.saveNewGoal(body);
    }

    @PutMapping("/{goalId}")
    @PreAuthorize("hasAnyAuthority('goal:CRUD', 'goal:U')")
    public Goal updateGoal(@PathVariable long goalId, @RequestBody @Validated GoalDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.goalsService.updateGoal(goalId, body);
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('goal:CRUD', 'goal:D')")
    public void deleteGoal(@PathVariable long goalId){
        this.goalsService.deleteGoal(goalId);
    }


}
