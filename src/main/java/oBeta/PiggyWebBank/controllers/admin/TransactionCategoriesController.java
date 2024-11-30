package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.BaseTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.admin.UserTransactionCategoryDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.TransactionCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction-categories")
@PreAuthorize("hasAnyAuthority(" +
        "'transaction-category:CRUD'," +
        "'base-transaction-category:CRUD'," +
        "'transaction-category:C'" +
        "'base-transaction-category:C'" +
        "'transaction-category:R'," +
        "'transaction-category:U'," +
        "'base-transaction-category:U'," +
        "'transaction-category:D'," +
        "'base-transaction-category:D'," +
        ")"
)
public class TransactionCategoriesController {

    @Autowired
    private TransactionCategoriesService transactionCategoriesService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('transaction-category:CRUD', 'transaction-category:R')")
    public Page<TransactionCategory> getAllTransactionCategorys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.transactionCategoriesService.getAllTransactionCategories(page, size, sortBy);
    }

    @GetMapping("/{transactionCategoryId}")
    @PreAuthorize("hasAnyAuthority('transaction-category:CRUD', 'transaction-category:R')")
    public TransactionCategory getTransactionCategoryById(@PathVariable long transactionCategoryId){
        return this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('transaction-category:CRUD', 'transaction-category:C')")
    public TransactionCategory saveNewUserTransactionCategory(@RequestBody @Validated UserTransactionCategoryDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.transactionCategoriesService.saveNewUserTransactionCategory(body);
    }

    @PostMapping("/base")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('base-transaction-category:CRUD', 'base-transaction-category:C')")
    public TransactionCategory saveNewBaseTransactionCategory(@RequestBody @Validated BaseTransactionCategoryDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.transactionCategoriesService.saveNewBaseTransactionCategory(body);
    }

    @PutMapping("/{transactionCategoryId}")
    @PreAuthorize("hasAnyAuthority('transaction-category:CRUD', 'transaction-category:U')")
    public TransactionCategory updateUserTransactionCategory(@PathVariable long transactionCategoryId, @RequestBody @Validated UserTransactionCategoryDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        this.getTransactionCategoryById(transactionCategoryId);

        return this.transactionCategoriesService.updateUserTransactionCategory(transactionCategoryId, body);
    }

    @PutMapping("/base/{transactionCategoryId}")
    @PreAuthorize("hasAnyAuthority('base-transaction-category:CRUD', 'base-transaction-category:U')")
    public TransactionCategory updateBaseTransactionCategory(@PathVariable long transactionCategoryId, @RequestBody @Validated BaseTransactionCategoryDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        TransactionCategory transactionCategory = this.getTransactionCategoryById(transactionCategoryId);

        if(transactionCategory.getUser() != null)
            throw new BadRequestException("Transaction category " + transactionCategory.getName() + " is linked to user " + transactionCategory.getUser().getUsername());

        return this.transactionCategoriesService.updateBaseTransactionCategory(transactionCategoryId, body);
    }

    @DeleteMapping("/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('transaction-category:CRUD', 'transaction-category:D')")
    public void deleteTransactionCategory(@PathVariable long transactionCategoryId){
        this.transactionCategoriesService.deleteUserTransactionCategoryAdmin(transactionCategoryId);
    }

    @DeleteMapping("/base/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('base-transaction-category:CRUD', 'base-transaction-category:D')")
    public void deleteBaseTransactionCategory(@PathVariable long transactionCategoryId){

        TransactionCategory transactionCategory = this.getTransactionCategoryById(transactionCategoryId);

        if(transactionCategory.getUser() != null)
            throw new BadRequestException("Transaction category " + transactionCategory.getName() + " is linked to user " + transactionCategory.getUser().getUsername());

        this.transactionCategoriesService.deleteUserTransactionCategoryAdmin(transactionCategoryId);
    }

}
