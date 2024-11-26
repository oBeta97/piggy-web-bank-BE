package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.FixedTransaction;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.interfaces.BaseFixedTransactionDTO;
import oBeta.PiggyWebBank.payloads.admin.FixedTransactionDTO;
import oBeta.PiggyWebBank.payloads.me.MeFixedTransactionDTO;
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

    public Page<FixedTransaction> getAllFixedTransactionsPagesByUser(User user, int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fixedTransactionsRepo.findByUser(user, pageable);
    }

    public FixedTransaction getFixedTransactionByIdAndUser(long id, User user){
        return this.fixedTransactionsRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Fixed transaction with " + id + " id not found!"));
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

    public FixedTransaction saveMeFixedTransaction(MeFixedTransactionDTO dto, User userLogged){
        FixedTransaction res =  this.fixedTransactionsRepo.save(
                new FixedTransaction(dto, userLogged)
        );

        // After save on fixed transaction the month history will be reloaded
        this.monthHistoriesService.reloadLastMonthHistoty(userLogged);

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

    public FixedTransaction updateMeFixedTransaction(long idToUpdate, MeFixedTransactionDTO dto, User loggedUser){

        FixedTransaction found = this.getFixedTransactionByIdAndUser(idToUpdate,loggedUser);

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

    public void deleteFixedTransaction(long idToDelete, User u){
        FixedTransaction found = this.getFixedTransactionByIdAndUser(idToDelete,u);

        this.fixedTransactionsRepo.delete(found);
        this.monthHistoriesService.reloadLastMonthHistoty(u);

    }

    private boolean isFoundEqualsToDTO(FixedTransaction found, BaseFixedTransactionDTO dto){
        return found.getPeriod() == dto.period() &&
                found.getAmount() == dto.amount() &&
                found.getName().equals(dto.name());
    }

}
