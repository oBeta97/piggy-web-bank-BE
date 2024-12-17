package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.payloads.me.MeUserDTO;
import oBeta.PiggyWebBank.payloads.me.UpdateMeMinimumSavingsDTO;
import oBeta.PiggyWebBank.payloads.me.UpdatePasswordDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@PreAuthorize("hasAnyAuthority(" +
        "'me:CRUD'," +
        "'me:R'," +
        "'me-role:R'," +
        "'me-characteristic:R'," +
        "'me:U'," +
        ")"
)
public class MeController {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('me:CRUD', 'me:R')")
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('me-role:R')")
    public Role getMeRole(@AuthenticationPrincipal User loggedUser){
        return loggedUser.getRole();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('me:CRUD', 'me:U')")
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
    @PreAuthorize("hasAnyAuthority('me:CRUD', 'me:U')")
    public void updateMePassword(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated UpdatePasswordDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        this.userService.updatePassword(body.password(), loggedUser);
    }

    @PatchMapping("/minimum-savings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('me:CRUD', 'me:U')")
    public void updateMeMinimumSavings(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated UpdateMeMinimumSavingsDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        this.userCharacteristicsService.updateMinimumSavings(body.minimumSavings(), loggedUser);
    }


    @GetMapping("/user-characteristics")
    @PreAuthorize("hasAuthority('me-characteristic:R')")
    public UserCharacteristic getMeUserCharacteristics(@AuthenticationPrincipal User loggedUser){
        return this.userCharacteristicsService.getUserCharacteristicByUser(loggedUser);
    }

}
