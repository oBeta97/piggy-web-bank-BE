package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.FixedTransactionDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.FixedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/fixed-transactions")
public class FixedTransactionController {

    @Autowired
    private FixedTransactionsService fixedTransactionsService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    public Page<FixedTransaction> getAllFixedTransaction(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.fixedTransactionsService.getAllFixedTransactions(page, size, sortBy);
    }

    @GetMapping("/{fixedTransactionId}")
    public FixedTransaction getFixedTransactionById(@PathVariable long fixedTransactionId){
        return this.fixedTransactionsService.getFixedTransactionById(fixedTransactionId);
    }


    @PostMapping
    public FixedTransaction saveNewFixedTransaction(@RequestBody @Validated FixedTransactionDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.fixedTransactionsService.saveNewFixedTransaction(body);
    }

    @PutMapping("/{fixedTransactionId}")
    // ONLY DEV can update a FixedTransaction!
    public FixedTransaction updateFixedTransaction(@PathVariable long fixedTransactionId, @RequestBody @Validated FixedTransactionDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.fixedTransactionsService.updateFixedTransaction(fixedTransactionId, body);
    }

    @DeleteMapping("/{fixedTransactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // ONLY DEV can delete a FixedTransaction!
    public void deleteFixedTransaction(@PathVariable long fixedTransactionId){

        FixedTransaction fixedTransaction = this.getFixedTransactionById(fixedTransactionId);

        this.fixedTransactionsService.delteFixedTransaction(fixedTransactionId, fixedTransaction.getUser());
    }


}
