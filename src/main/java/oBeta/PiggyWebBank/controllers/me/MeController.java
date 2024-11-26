package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.me.MeFixedTransactionDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.FixedTransactionsService;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private FixedTransactionsService fixedTransactionsService;

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @GetMapping
    public User getMe(@AuthenticationPrincipal User loggedUser){
        return loggedUser;
    }


    @GetMapping("/fixed-transactions")
    public Page<FixedTransaction> getAllMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.fixedTransactionsService.getAllFixedTransactionsPagesByUser(loggedUser, page, size, sortBy);
    }

    @GetMapping("/fixed-transactions/{fixedTransactionId}")
    public FixedTransaction getMeFixedTransactionById(@AuthenticationPrincipal User loggedUser, @PathVariable long fixedTransactionId){
        FixedTransaction res = this.fixedTransactionsService.getFixedTransactionById(fixedTransactionId);

        this.userValidation.validateUser(loggedUser, res, FixedTransaction.class, fixedTransactionId);

        return res;
    }

    @PostMapping("/fixed-transactions")
    public FixedTransaction setMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeFixedTransactionDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.fixedTransactionsService.saveMeFixedTransaction(body, loggedUser);
    }

}
