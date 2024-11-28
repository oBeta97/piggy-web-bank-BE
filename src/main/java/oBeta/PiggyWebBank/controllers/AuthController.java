package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.signin.SigninDTO;
import oBeta.PiggyWebBank.payloads.login.LoginDTO;
import oBeta.PiggyWebBank.payloads.login.LoginResponseDTO;
import oBeta.PiggyWebBank.payloads.signin.SigninResponseDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.AuthService;
import oBeta.PiggyWebBank.services.MonthHistoriesService;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    @Autowired
    private ValidationControl validationControl;

    @PostMapping("/login")
    public LoginResponseDTO login (@RequestBody @Validated LoginDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return new LoginResponseDTO(
                this.authService.checkLoginCredentials(body)
        );
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.CREATED)
    public SigninResponseDTO signin(@RequestBody @Validated SigninDTO dto, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        User userInserted = this.userService.signin(dto);

        this.userCharacteristicsService.saveNewUserCharacteristic("€", userInserted);
        this.monthHistoriesService.saveNewMonthHistory(userInserted);

        return new SigninResponseDTO(
                userInserted.getName(),
                userInserted.getSurname(),
                userInserted.getUsername(),
                userInserted.getEmail()
            );
    }
}
