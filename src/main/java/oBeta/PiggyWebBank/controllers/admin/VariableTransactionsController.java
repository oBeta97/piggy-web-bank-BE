package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.VariableTransaction;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.VariableTransactionDTO;
import oBeta.PiggyWebBank.services.VariableTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/variable-transactions")
public class VariableTransactionsController {

    @Autowired
    private VariableTransactionsService variableTransactionsService;

    @GetMapping
    public Page<VariableTransaction> getAllVariableTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.variableTransactionsService.getAllVariableTransactions(page, size, sortBy);
    }

    @GetMapping("/{variableTransactionId}")
    public VariableTransaction getVariableTransactionById(@PathVariable long variableTransactionId){
        return this.variableTransactionsService.getVariableTransactionById(variableTransactionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariableTransaction saveNewVariableTransaction(@RequestBody @Validated VariableTransactionDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.variableTransactionsService.saveNewVariableTransaction(body);
    }

    @PutMapping("/{variableTransactionId}")
    public VariableTransaction updateVariableTransaction(@PathVariable long variableTransactionId, @RequestBody @Validated VariableTransactionDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.variableTransactionsService.updateVariableTransaction(variableTransactionId, body);
    }

    @DeleteMapping("/{variableTransactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // ONLY DEV can delete a VariableTransaction!
    public void deleteVariableTransaction(@PathVariable long variableTransactionId){
        VariableTransaction variableTransaction = this.getVariableTransactionById(variableTransactionId);

        this.variableTransactionsService.delteVariableTransaction(variableTransactionId, variableTransaction.getUser());
    }


}
