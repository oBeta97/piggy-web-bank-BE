package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.BaseTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.admin.UserTransactionCategoryDTO;
import oBeta.PiggyWebBank.services.TransactionCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction-categories")
public class TransactionCategoriesController {

    @Autowired
    private TransactionCategoriesService transactionCategoriesService;

    @GetMapping
    public Page<TransactionCategory> getAllTransactionCategorys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.transactionCategoriesService.getAllTransactionCategories(page, size, sortBy);
    }

    @GetMapping("/{transactionCategoryId}")
    public TransactionCategory getTransactionCategoryById(@PathVariable long transactionCategoryId){
        return this.transactionCategoriesService.getTransactionCategoryById(transactionCategoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // USER can save a new TransactionCategory!
    public TransactionCategory saveNewUserTransactionCategory(@RequestBody @Validated UserTransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.transactionCategoriesService.saveNewUserTransactionCategory(body);
    }

    @PostMapping("/base")
    @ResponseStatus(HttpStatus.CREATED)
    // ADMIN can save a new base TransactionCategory!
    public TransactionCategory saveNewBaseTransactionCategory(@RequestBody @Validated BaseTransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.transactionCategoriesService.saveNewBaseTransactionCategory(body);
    }

    @PutMapping("/{transactionCategoryId}")
    // USER can update a TransactionCategory!
    public TransactionCategory updateUserTransactionCategory(@PathVariable long transactionCategoryId, @RequestBody @Validated UserTransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        this.getTransactionCategoryById(transactionCategoryId);

        return this.transactionCategoriesService.updateUserTransactionCategory(transactionCategoryId, body);
    }

    @PutMapping("/base/{transactionCategoryId}")
    // ADMIN can update a TransactionCategory!
    public TransactionCategory updateBaseTransactionCategory(@PathVariable long transactionCategoryId, @RequestBody @Validated BaseTransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        TransactionCategory transactionCategory = this.getTransactionCategoryById(transactionCategoryId);

        if(transactionCategory.getUser() != null)
            throw new BadRequestException("Transaction category " + transactionCategory.getName() + " is linked to user " + transactionCategory.getUser().getUsername());

        return this.transactionCategoriesService.updateBaseTransactionCategory(transactionCategoryId, body);
    }

    @DeleteMapping("/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // USER can update a TransactionCategory!
    public void deleteTransactionCategory(@PathVariable long transactionCategoryId){
        this.transactionCategoriesService.deleteUserTransactionCategoryAdmin(transactionCategoryId);
    }

    @DeleteMapping("/base/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // ADMIN can update a TransactionCategory!
    public void deleteBaseTransactionCategory(@PathVariable long transactionCategoryId){

        TransactionCategory transactionCategory = this.getTransactionCategoryById(transactionCategoryId);

        if(transactionCategory.getUser() != null)
            throw new BadRequestException("Transaction category " + transactionCategory.getName() + " is linked to user " + transactionCategory.getUser().getUsername());


        this.transactionCategoriesService.deleteUserTransactionCategoryAdmin(transactionCategoryId);
    }

}
