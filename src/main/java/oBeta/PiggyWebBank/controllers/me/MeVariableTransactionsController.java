package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.VariableTransaction;
import oBeta.PiggyWebBank.payloads.admin.VariableTransactionDTO;
import oBeta.PiggyWebBank.payloads.me.MeVariableTransactionDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.VariableTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/variable-transactions")
public class MeVariableTransactionsController {

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private VariableTransactionsService variableTransactionsService;

    @GetMapping
    public Page<VariableTransaction> getAllMeVariableTransasctions(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.variableTransactionsService.getAllUserVariableTransactions(page, size, sortBy, loggedUser);
    }

    @GetMapping("/{variableTransactionId}")
    public VariableTransaction getMeVariableTransaction(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long variableTransactionId
    ){
        VariableTransaction res = this.variableTransactionsService.getVariableTransactionById(variableTransactionId);

        this.userValidation.validateUser(loggedUser, res, VariableTransaction.class, variableTransactionId);

        return res;
    }

    @PostMapping
    public VariableTransaction addMeVariableTransaction(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeVariableTransactionDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.variableTransactionsService.saveNewVariableTransaction(
                new VariableTransactionDTO(body.name(), body.amount(), body.transactionDt(), body.transactionCategory_id(), loggedUser.getId().toString())
        );
    }


    @PutMapping("/{variableTransactionId}")
    public VariableTransaction updateMeVariableTransaction(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long variableTransactionId,
            @RequestBody @Validated MeVariableTransactionDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        VariableTransaction vt = this.variableTransactionsService.getVariableTransactionById(variableTransactionId);

        this.userValidation.validateUser(loggedUser, vt, VariableTransaction.class, variableTransactionId);

        return this.variableTransactionsService.updateVariableTransaction(
                    variableTransactionId,
                    new VariableTransactionDTO(body.name(), body.amount(), body.transactionDt(), body.transactionCategory_id(), loggedUser.getId().toString())
                );

    }

    @DeleteMapping("/{variableTransactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeVariableTransaction(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long variableTransactionId
    ){
        VariableTransaction vt = this.variableTransactionsService.getVariableTransactionById(variableTransactionId);

        this.userValidation.validateUser(loggedUser, vt, VariableTransaction.class, variableTransactionId);

        this.variableTransactionsService.delteVariableTransaction(variableTransactionId, loggedUser);

    }

}
