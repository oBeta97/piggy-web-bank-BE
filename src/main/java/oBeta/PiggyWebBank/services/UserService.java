package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepo;

    public Page<User> getAllUsers(int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.featuresRepo.findAll(pageable);
    }

}
