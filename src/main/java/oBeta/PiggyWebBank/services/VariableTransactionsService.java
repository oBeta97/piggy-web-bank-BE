package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.entities.VariableTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.VariableTransactionDTO;
import oBeta.PiggyWebBank.repositories.VariableTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VariableTransactionsService {
    @Autowired
    private VariableTransactionsRepository variableTransactionsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionCategoriesService transactionCategoriesService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    public Page<VariableTransaction> getAllVariableTransactions(int page, int size, String sortBy) {
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.variableTransactionsRepo.findAll(pageable);
    }

    public VariableTransaction getVariableTransactionById(long idToFind){
        return this.variableTransactionsRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("Fixed transaction with id " + idToFind + " not found!")
                );
    }

    public List<VariableTransaction> getVariableEarningsByUserOfThisMonth(User user){
        return this.variableTransactionsRepo.findVariableEarningsByUserOfThisMonth(user);
    }

    public List<VariableTransaction> getVariableExpensesByUSerOfThisMonth(User user){
        return this.variableTransactionsRepo.findVariableExpensesByUserOfThisMonth(user);
    }


    public VariableTransaction saveNewVariableTransaction(VariableTransactionDTO dto){
        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }

        TransactionCategory transactionCategory = this.transactionCategoriesService.getTransactionCategoryById(dto.transactionCategory_id());

        VariableTransaction res = this.variableTransactionsRepo.save(
                new VariableTransaction(dto, transactionCategory, user)
        );

        monthHistoriesService.reloadLastMonthHistoty(user);

        return res;
    }

    public VariableTransaction updateVariableTransaction(long idToUpdate, VariableTransactionDTO dto){

        VariableTransaction found = this.getVariableTransactionById(idToUpdate);
        if (!found.getUser().getId().equals(UUID.fromString(dto.user_id()))) throw new BadRequestException("Payload error! Wrong user");

        TransactionCategory transactionCategory = this.transactionCategoriesService.getTransactionCategoryById(dto.transactionCategory_id());

        found.setTransactionDt(dto.transactionDt());
        found.setAmount(dto.amount());
        found.setName(dto.name());
        found.setTransactionCategory(transactionCategory);

        return this.variableTransactionsRepo.save(found);
    }

    public void delteVariableTransaction(long idToDelete, User u){
        VariableTransaction found = this.getVariableTransactionById(idToDelete);

        if(found.getUser().getId() != u.getId()) throw new BadRequestException("Request error! Wrong user");

        this.variableTransactionsRepo.delete(found);
    }
}
