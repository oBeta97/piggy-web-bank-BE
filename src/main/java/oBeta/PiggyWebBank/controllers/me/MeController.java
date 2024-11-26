package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @GetMapping
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }

    @GetMapping("/roles")
    public Role getMeRole(@AuthenticationPrincipal User loggedUser){
        return loggedUser.getRole();
    }

    @GetMapping("/user-characteristics")
    public UserCharacteristic getMeUserCharacteristics(@AuthenticationPrincipal User loggedUser){
        return this.userCharacteristicsService.getUserCharacteristicByUser(loggedUser);
    }

}
