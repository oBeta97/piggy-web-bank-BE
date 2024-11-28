package oBeta.PiggyWebBank.controllers.me;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.me.MeFixedTransactionDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.FixedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/fixed-transactions")
public class MeFixedTransactionsController {

    @Autowired
    private FixedTransactionsService fixedTransactionsService;

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @GetMapping
    public Page<FixedTransaction> getAllMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.fixedTransactionsService.getAllFixedTransactionsPagesByUser(loggedUser, page, size, sortBy);
    }

    @GetMapping("/{fixedTransactionId}")
    public FixedTransaction getMeFixedTransactionById(@AuthenticationPrincipal User loggedUser, @PathVariable long fixedTransactionId){
        FixedTransaction res = this.fixedTransactionsService.getFixedTransactionById(fixedTransactionId);

        this.userValidation.validateUser(loggedUser, res, FixedTransaction.class, fixedTransactionId);

        return res;
    }

    @PostMapping("")
    public FixedTransaction setMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeFixedTransactionDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.fixedTransactionsService.saveMeFixedTransaction(body, loggedUser);
    }

    @PutMapping("/{fixedTransactionId}")
    public FixedTransaction updateMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeFixedTransactionDTO body,
            BindingResult validationResult,
            @PathVariable long fixedTransactionId
    ){
        this.validationControl.checkErrors(validationResult);

        return this.fixedTransactionsService.updateMeFixedTransaction(fixedTransactionId, body, loggedUser);
    }

    @DeleteMapping("/{fixedTransactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeFixedTransaction(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long fixedTransactionId
    ){
        this.fixedTransactionsService.deleteFixedTransaction(fixedTransactionId, loggedUser);
    }

}
