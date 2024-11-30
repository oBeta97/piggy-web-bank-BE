package oBeta.PiggyWebBank.controllers.admin;

import oBeta.PiggyWebBank.entities.*;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.UserDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority(" +
        "'user:CRUD'," +
        "'user:C'" +
        "'user:R'," +
        "'user-role:R'," +
        "'user:U'," +
        "'user:D'," +
        ")"
)
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:CRUD', 'user:R')")
    public Page<User> getAllUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.userService.getAllUsersPage(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('user:CRUD', 'user:R')")
    public User getUserById(@PathVariable UUID userId){
        return this.userService.getUserById(userId);
    }

    @GetMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('user-role:R')")
    public Role getUserRole(@PathVariable UUID userId){
        User u = this.userService.getUserById(userId);
        return u.getRole();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('user:CRUD', 'user:C')")
    public User saveNewUser (@RequestBody @Validated UserDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        User res = this.userService.saveNewUser(body);

        this.userCharacteristicsService.saveNewUserCharacteristic("€", res);
        this.monthHistoriesService.saveNewMonthHistory(res);

        return res;
    }


    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('user:CRUD', 'user:U')")
    public User updateUser(@PathVariable UUID userId, @RequestBody @Validated UserDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.userService.updateUser(userId, body);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('user:CRUD', 'user:D')")
    public void deleteUser(@PathVariable UUID userId){
        this.userService.deleteUser(userId);
    }

}
