package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.TransactionCategory;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.admin.BaseTransactionCategoryDTO;
import oBeta.PiggyWebBank.payloads.admin.UserTransactionCategoryDTO;
import oBeta.PiggyWebBank.repositories.TransactionCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionCategoriesService {

    @Autowired
    private TransactionCategoriesRepository transactionCategoriesRepo;

    @Autowired
    private UserService userService;

    public Page<TransactionCategory> getAllTransactionCategories (int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.transactionCategoriesRepo.findAll(pageable);
    }

    public TransactionCategory getTransactionCategoryById(long idToFind){
        return this.transactionCategoriesRepo.findById(idToFind)
                .orElseThrow(() -> new NotFoundException("Transaction Category with id " + idToFind + " not found!" ));
    }

    public List<TransactionCategory> getBaseTransactionCategory(){
        return this.transactionCategoriesRepo.findBaseTransactionCategory();
    }

    public List<TransactionCategory> getTransactionCategoryOfAnUser(User user){
        return this.transactionCategoriesRepo.findAllTransactionCategoryOfAnUser(user);
    }

    // ONLY admin users can use this method
    public TransactionCategory saveNewBaseTransactionCategory(BaseTransactionCategoryDTO dto){
        return this.transactionCategoriesRepo.save(
                new TransactionCategory(dto)
        );
    }

    public TransactionCategory saveNewUserTransactionCategory(UserTransactionCategoryDTO dto){
        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }

        this.transactionCategoriesRepo.findByUserAndNameAndIsExpense(user, dto.name(), dto.isExpense()).
                ifPresent(transactionCategory -> {
                    throw new BadRequestException("The user " + user.getUsername() + " alredy have the " + (dto.isExpense() ? "expense" : "earning") + " named " + dto.name());
                });

        return this.transactionCategoriesRepo.save(
                new TransactionCategory(dto, user)
        );
    }

    public TransactionCategory updateUserTransactionCategory(long idToUpdate, UserTransactionCategoryDTO dto){
        User user;

        try{
            user = this.userService.getUserById(UUID.fromString(dto.user_id()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("User id format not valid!");
        }

        TransactionCategory found = this.getTransactionCategoryById(idToUpdate);

        if (user.getId() != found.getUser().getId()) throw new BadRequestException("Payload error! Wrong user");

        this.transactionCategoriesRepo.findByUserAndNameAndIsExpense(user, dto.name(), dto.isExpense()).
                ifPresent(transactionCategory -> {
                    if(transactionCategory.getId() != found.getId())
                        throw new BadRequestException("The user " + user.getUsername() + " alredy have the " + (dto.isExpense() ? "expense" : "earning") + " named " + dto.name());
                });

        if(this.isFoundEqualsToDTO(found, dto))
            return found;

        found.setName(dto.name());
        found.setIsExpense(dto.isExpense());

        return this.transactionCategoriesRepo.save(found);
    }

    // ONLY admin users can use this method
    public TransactionCategory updateBaseTransactionCategory(long idToUpdate, BaseTransactionCategoryDTO dto){
        TransactionCategory found = this.getTransactionCategoryById(idToUpdate);

        if (found.getUser() != null) throw new BadRequestException("The transaction category provided is associated to a user");

        if(this.isFoundEqualsToDTO(found, dto))
            return found;

        found.setName(dto.name());
        found.setIsExpense(dto.isExpense());

        return this.transactionCategoriesRepo.save(found);
    }

    public void deleteBaseTransactionCategory(long idToDelete){
        TransactionCategory found = this.getTransactionCategoryById(idToDelete);

        if (found.getUser() != null) throw new BadRequestException("The transaction category provided is associated to a user");

        this.transactionCategoriesRepo.delete(found);
    }

//    TODO - togliere i commenti, sono predisposizioni per il controllo del JWT
    public void deleteUserTransactionCategory(long idToDelete/*, User user*/){
//        User u = this.userService.getUserById(user.getId());

        TransactionCategory found = this.getTransactionCategoryById(idToDelete);

//        if (u.getId() != found.getUser().getId()) throw new BadRequestException("Request error! Wrong user");

        this.transactionCategoriesRepo.delete(found);
    }

    private boolean isFoundEqualsToDTO(TransactionCategory found, BaseTransactionCategoryDTO dto){
        return found.getName().equals(dto.name()) &&
                found.getIsExpense() == dto.isExpense();
    }

    private boolean isFoundEqualsToDTO(TransactionCategory found, UserTransactionCategoryDTO dto){
        return found.getName().equals(dto.name()) &&
                found.getIsExpense() == dto.isExpense();
    }
}
