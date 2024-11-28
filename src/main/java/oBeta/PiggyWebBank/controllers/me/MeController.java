package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.payloads.me.MeUserDTO;
import oBeta.PiggyWebBank.payloads.me.UpdatePasswordDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    private UserService userService;

    @Autowired
    protected ValidationControl validationControl;

    @GetMapping
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }

    @GetMapping("/roles")
    public Role getMeRole(@AuthenticationPrincipal User loggedUser){
        return loggedUser.getRole();
    }

    @PutMapping
    public User updateMe(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeUserDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.userService.updateMeUser(body,loggedUser);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMePassword(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated UpdatePasswordDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        this.userService.updatePassword(body.password(), loggedUser);
    }

    @GetMapping("/user-characteristics")
    public UserCharacteristic getMeUserCharacteristics(@AuthenticationPrincipal User loggedUser){
        return this.userCharacteristicsService.getUserCharacteristicByUser(loggedUser);
    }

}
