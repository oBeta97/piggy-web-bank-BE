package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.FeatureDTO;
import oBeta.PiggyWebBank.payloads.FixedTransactionDTO;
import oBeta.PiggyWebBank.services.FixedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixedtransaction")
public class FixedTransactionController {

    @Autowired
    FixedTransactionsService fixedTransactionsService;

    @GetMapping
    public Page<FixedTransaction> getAllFixedTransaction(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.fixedTransactionsService.getAllFixedTransactions(page, size, sortBy);
    }

    @PostMapping
    public FixedTransaction saveNewFeature(@RequestBody @Validated FixedTransactionDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload della fattura!");
        }

        return this.fixedTransactionsService.saveNewFixedTransaction(body);
    }

}
