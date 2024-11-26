package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.admin.FixedTransactionDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.FixedTransactionsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FixedTransactionsService fixedTransactionsService;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }

    @GetMapping("/fixed-transactions/{fixedTransactionId}")
    public FixedTransaction getMeFixedTransactionById(@AuthenticationPrincipal User loggedUser, @PathVariable long fixedTransactionId){
        FixedTransaction res = this.fixedTransactionsService.getFixedTransactionById(fixedTransactionId);

        userValidation.validateUser(loggedUser, res, FixedTransaction.class, fixedTransactionId);

        return res;
    }

    @PostMapping("/fixed-transactions")
    public FixedTransaction setMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated FixedTransactionDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);
        this.userValidation.validateUser(loggedUser, body);

        return this.fixedTransactionsService.saveNewFixedTransaction(body);
    }

}
