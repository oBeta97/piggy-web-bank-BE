package oBeta.PiggyWebBank.controllers;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.UserDTO;
import oBeta.PiggyWebBank.payloads.UserDTO;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //ADMIN can create new users
    public User saveNewUser (@RequestBody @Validated UserDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.userService.saveNewUser(body);
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
