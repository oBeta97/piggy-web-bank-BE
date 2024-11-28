package oBeta.PiggyWebBank.controllers.me;


import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.admin.UserTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.me.MeTransactionCategoryDTO;
import oBeta.PiggyWebBank.security.UserValidation;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.TransactionCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/transaction-categories")
public class MeTransactionCategoriesController {

    @Autowired
    private TransactionCategoriesService transactionCategoriesService;

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @GetMapping
    public List<TransactionCategory> getMeTransactionCategories(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean usersCategoriesOnly
    ){

        if (usersCategoriesOnly)
            return this.transactionCategoriesService.getTransactionCategoryOfUserOnly(loggedUser);

        return this.transactionCategoriesService.getTransactionCategoryOfAnUser(loggedUser);
    }

    @GetMapping("/{transactionCategoryId}")
    public TransactionCategory getMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long transactionCategoryId
    ){
        TransactionCategory tc = this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);

        this.userValidation.validateUser(loggedUser, tc, TransactionCategory.class, transactionCategoryId);

        return tc;
    }

    @PostMapping
    public TransactionCategory addMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Validated MeTransactionCategoryDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.transactionCategoriesService.saveNewUserTransactionCategory(
               new UserTransactionCategoryDTO(body.name(), body.isExpense(), loggedUser.getId().toString())
        );
    }

    @PutMapping("/{transactionCategoryId}")
    public TransactionCategory updateMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long transactionCategoryId,
            @RequestBody @Validated MeTransactionCategoryDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        TransactionCategory tc = this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);

        this.userValidation.validateUser(loggedUser, tc, TransactionCategory.class, transactionCategoryId);

        return this.transactionCategoriesService.updateUserTransactionCategory(
                transactionCategoryId,
                new UserTransactionCategoryDTO(body.name(), body.isExpense(), loggedUser.getId().toString())
        );
    }

    @DeleteMapping("/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long transactionCategoryId
    ){
        TransactionCategory tc = this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);

        this.userValidation.validateUser(loggedUser, tc, TransactionCategory.class, transactionCategoryId);

        this.transactionCategoriesService.deleteUserTransactionCategory(transactionCategoryId, loggedUser);
    }

}
