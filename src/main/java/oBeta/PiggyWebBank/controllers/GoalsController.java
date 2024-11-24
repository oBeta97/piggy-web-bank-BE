package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.Goal;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.GoalDTO;
import oBeta.PiggyWebBank.services.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/goals")
public class GoalsController {

    @Autowired
    private GoalsService goalsService;


    @GetMapping
    public Page<Goal> getAllGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.goalsService.getAllGoals(page, size, sortBy);
    }

    @GetMapping("/{goalId}")
    public Goal getGoalById(@PathVariable long goalId){
        return this.goalsService.getGoalById(goalId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // USER can save a new Goal!
    public Goal saveNewGoal(@RequestBody @Validated GoalDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.goalsService.saveNewGoal(body);
    }

    @PutMapping("/{goalId}")
    // USER can update a Goal!
    public Goal updateGoal(@PathVariable long goalId, @RequestBody @Validated GoalDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.goalsService.updateGoal(goalId, body);
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // USER can update a Goal!
    public void deleteGoal(@PathVariable long goalId){
        this.goalsService.deleteGoal(goalId);
    }


}
