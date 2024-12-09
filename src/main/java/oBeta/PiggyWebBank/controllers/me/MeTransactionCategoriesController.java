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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/transaction-categories")
@PreAuthorize("hasAnyAuthority(" +
        "'me-transaction-category:CRUD'," +
        "'me-transaction-category:C'" +
        "'me-transaction-category:R'," +
        "'me-transaction-category:U'," +
        "'me-transaction-category:D'," +
        ")"
)
public class MeTransactionCategoriesController {

    @Autowired
    private TransactionCategoriesService transactionCategoriesService;

    @Autowired
    private ValidationControl validationControl;

    @Autowired
    private UserValidation userValidation;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('me-transaction-category:CRUD', 'me-transaction-category:R')")
    public List<TransactionCategory> getMeTransactionCategories(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(defaultValue = "false") boolean usersCategoriesOnly
    ){

        if (usersCategoriesOnly)
            return this.transactionCategoriesService.getTransactionCategoryOfUserOnly(loggedUser);

        return this.transactionCategoriesService.getTransactionCategoryOfAnUser(loggedUser);
    }

    @GetMapping("/{transactionCategoryId}")
    @PreAuthorize("hasAnyAuthority('me-transaction-category:CRUD', 'me-transaction-category:R')")
    public TransactionCategory getMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long transactionCategoryId
    ){
        TransactionCategory tc = this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);

        this.userValidation.validateUser(loggedUser, tc, TransactionCategory.class, transactionCategoryId);

        return tc;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('me-transaction-category:CRUD', 'me-transaction-category:C')")
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
    @PreAuthorize("hasAnyAuthority('me-transaction-category:CRUD', 'me-transaction-category:U')")
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
    @PreAuthorize("hasAnyAuthority('me-transaction-category:CRUD', 'me-transaction-category:D')")
    public void deleteMeTransactionCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable long transactionCategoryId
    ){
        TransactionCategory tc = this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);

        this.userValidation.validateUser(loggedUser, tc, TransactionCategory.class, transactionCategoryId);

        this.transactionCategoriesService.deleteUserTransactionCategory(transactionCategoryId, loggedUser);
    }

}
