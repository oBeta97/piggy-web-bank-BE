package oBeta.PiggyWebBank.controllers.admin;

import oBeta.PiggyWebBank.entities.*;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.UserDTO;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @GetMapping
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.userService.getAllUsers(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        return this.userService.getUserById(userId);
    }

    @GetMapping("/{userId}/role")
    public Role getUserRole(@PathVariable UUID userId){
        User u = this.userService.getUserById(userId);
        return u.getRole();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //ADMIN can create new users
    public User saveNewUser (@RequestBody @Validated UserDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        User res = this.userService.saveNewUser(body);

        this.userCharacteristicsService.saveNewUserCharacteristic("€", res);
        this.monthHistoriesService.saveNewMonthHistory(res);

        return res;
    }


    @PutMapping("/{userId}")
    // USER can update a User!
    public User updateUser(@PathVariable UUID userId, @RequestBody @Validated UserDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.userService.updateUser(userId, body);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // USER can delete a User!
    public void deleteUser(@PathVariable UUID userId){
        this.userService.deleteUser(userId);
    }

}
