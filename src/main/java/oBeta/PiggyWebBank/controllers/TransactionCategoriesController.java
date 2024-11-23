package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.TransactionCategoryDTO;
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
    public TransactionCategory saveNewTransactionCategory(@RequestBody @Validated TransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.transactionCategoriesService.saveNewUserTransactionCategory(body);
    }

    @PutMapping("/{transactionCategoryId}")
    // USER can update a TransactionCategory!
    public TransactionCategory updateTransactionCategory(@PathVariable long transactionCategoryId, @RequestBody @Validated TransactionCategoryDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.transactionCategoriesService.updateUserTransactionCategory(transactionCategoryId, body);
    }

    @DeleteMapping("/{transactionCategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // USER can update a TransactionCategory!
    public void deleteTransactionCategory(@PathVariable long transactionCategoryId){
        this.transactionCategoriesService.deleteUserTransactionCategory(transactionCategoryId);
    }


}
