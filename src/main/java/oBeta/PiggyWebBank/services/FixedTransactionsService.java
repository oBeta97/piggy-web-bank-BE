package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.FixedTransactionDTO;
import oBeta.PiggyWebBank.repositories.FixedTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FixedTransactionsService {

    @Autowired
    private FixedTransactionsRepository fixedTransactionsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MonthHistoriesService monthHistoriesService;

    public Page<FixedTransaction> getAllFixedTransactions(int page, int size, String sortBy) {
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fixedTransactionsRepo.findAll(pageable);
    }

    public List<FixedTransaction> getAllUserFixedTransactions(User user){
        return this.fixedTransactionsRepo.findByUser(user);
    }


    public List<FixedTransaction> getFixedEarningsByUser(User user){
        return this.fixedTransactionsRepo.findFixedEarningByUser(user);
    }

    public List<FixedTransaction> getFixedExpensesByUSer(User user){
        return this.fixedTransactionsRepo.findFixedExpensesByUser(user);
    }

    public FixedTransaction getFixedTransactionById(long idToFind){
        return this.fixedTransactionsRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("Fixed transaction with id " + idToFind + " not found!")
                );
    }

    public FixedTransaction saveNewFixedTransaction(FixedTransactionDTO dto){

        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }
        FixedTransaction res =  this.fixedTransactionsRepo.save(
                new FixedTransaction(dto, user)
        );

        // After save on fixed transaction the month history will be reloaded
        this.monthHistoriesService.reloadLastMonthHistoty(user);

        return res;
    }

    public FixedTransaction updateFixedTransaction(long idToUpdate, FixedTransactionDTO dto){

        FixedTransaction found = this.getFixedTransactionById(idToUpdate);

        if (!found.getUser().getId().equals(UUID.fromString(dto.user_id())))
            throw new BadRequestException("Payload error! Wrong user");

        // if there's not changes the update won't be done
        if(this.isFoundEqualsToDTO(found, dto))
            return found;

        found.setPeriod(dto.period());
        found.setAmount(dto.amount());
        found.setName(dto.name());

        FixedTransaction res =  this.fixedTransactionsRepo.save(found);

        // After save on fixed transaction the month history will be reloaded
        this.monthHistoriesService.reloadLastMonthHistoty(found.getUser());

        return res;
    }

    public void delteFixedTransaction(long idToDelete, User u){
        FixedTransaction found = this.getFixedTransactionById(idToDelete);

        if(found.getUser().getId() != u.getId()) throw new BadRequestException("Request error! Wrong user");

        this.fixedTransactionsRepo.delete(found);
        this.monthHistoriesService.reloadLastMonthHistoty(u);

    }

    private boolean isFoundEqualsToDTO(FixedTransaction found, FixedTransactionDTO dto){
        return found.getPeriod() == dto.period() &&
                found.getAmount() == dto.amount() &&
                found.getName().equals(dto.name());
    }

}
