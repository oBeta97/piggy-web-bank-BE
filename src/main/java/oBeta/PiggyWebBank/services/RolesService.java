package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.RoleDTO;
import oBeta.PiggyWebBank.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepo;

    @Autowired
    private FeaturesService featuresService;

    public Page<Role> getAllRoles (int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.rolesRepo.findAll(pageable);
    }

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

    public Role saveNewRole(RoleDTO roleDTO){

        this.rolesRepo.findByName(roleDTO.name())
                .ifPresent(feature -> {
                    throw new BadRequestException("Role with name " + feature.getName() + " already exist!");
                });

        List<Feature> featureList = this.featuresService.getFeaturesByList(roleDTO.roleList());

        return this.rolesRepo.save(new Role(roleDTO.name(), featureList));
    }

    public Role updateRole (long idToUpdate, RoleDTO roleDTO){

        Role found = this.getRoleById(idToUpdate);

        found.setName(roleDTO.name());

        List<Feature> featureList = this.featuresService.getFeaturesByList(roleDTO.roleList());
        found.setFeatureList(featureList);

        return this.rolesRepo.save(found);
    }

    public void deleteFeature (long idToDelete){
        this.rolesRepo.delete(
                this.getRoleById(idToDelete)
        );
    }

}
