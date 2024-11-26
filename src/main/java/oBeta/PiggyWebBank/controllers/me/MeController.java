package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeController {

    @GetMapping
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }

}
