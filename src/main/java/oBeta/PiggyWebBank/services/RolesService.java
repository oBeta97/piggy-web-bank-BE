package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepo;

    public Role getRoleById (long idToFind){
        return this.rolesRepo.findById(idToFind)
                .orElseThrow(() -> new NotFoundException("Role with id " + idToFind + " not found!" ));
    }

    public List<Role> getRolesByList(List<Long> idList){

        List<Role> res = new ArrayList<>();

        for (long idToFind : idList){
            res.add(this.getRoleById(idToFind));
        }

        return res;
    }
}
