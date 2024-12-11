package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.VariableTransaction;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.VariableTransactionDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.VariableTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/variable-transactions")
@PreAuthorize("hasAnyAuthority(" +
        "'variable-transaction:CRUD'," +
        "'variable-transaction:C'" +
        "'variable-transaction:R'," +
        "'variable-transaction:U'," +
        "'variable-transaction:D'," +
        ")"
)
public class VariableTransactionsController {

    @Autowired
    private VariableTransactionsService variableTransactionsService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    public Page<VariableTransaction> getAllVariableTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.variableTransactionsService.getAllVariableTransactions(page, size, sortBy);
    }

    @GetMapping("/{variableTransactionId}")
    @PreAuthorize("hasAnyAuthority('variable-transaction:CRUD', 'variable-transaction:R')")
    public VariableTransaction getVariableTransactionById(@PathVariable long variableTransactionId){
        return this.variableTransactionsService.getVariableTransactionById(variableTransactionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('variable-transaction:CRUD', 'variable-transaction:C')")
    public VariableTransaction saveNewVariableTransaction(@RequestBody @Validated VariableTransactionDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.variableTransactionsService.saveNewVariableTransaction(body);
    }

    @PutMapping("/{variableTransactionId}")
    @PreAuthorize("hasAnyAuthority('variable-transaction:CRUD', 'variable-transaction:U')")
    public VariableTransaction updateVariableTransaction(@PathVariable long variableTransactionId, @RequestBody @Validated VariableTransactionDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.variableTransactionsService.updateVariableTransaction(variableTransactionId, body);
    }

    @DeleteMapping("/{variableTransactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('variable-transaction:CRUD', 'variable-transaction:D')")
    public void deleteVariableTransaction(@PathVariable long variableTransactionId){
        VariableTransaction variableTransaction = this.getVariableTransactionById(variableTransactionId);

        this.variableTransactionsService.delteVariableTransaction(variableTransactionId, variableTransaction.getUser());
    }


}
