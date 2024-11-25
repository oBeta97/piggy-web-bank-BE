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
import java.util.stream.Collectors;

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

        List<Feature> featureList = this.featuresService.getFeaturesByList(roleDTO.featureList());
        Role role = new Role(roleDTO.name(), featureList);

        for (Feature feature : featureList) {
            feature.getRoleList().add(role);
        }

        return rolesRepo.save(role);

    }

    public Role updateRole (long idToUpdate, RoleDTO roleDTO){

        Role found = this.getRoleById(idToUpdate);

        this.rolesRepo.findByName(roleDTO.name()).ifPresent(role ->{
            if(role.getId() != idToUpdate)
                throw new BadRequestException("Role with name " + role.getName() + " already exist!");
        });

        List<Feature> dtoFeatureList = this.featuresService.getFeaturesByList(roleDTO.featureList());

        // if there's not changes the update won't be done
        if(this.isFoundEqualsToDTO(found, roleDTO, dtoFeatureList))
            return found;

        found.setName(roleDTO.name());

        found.getFeatureList().forEach(feature -> feature.getRoleList().remove(found));

        for (Feature feature : dtoFeatureList) {
            feature.getRoleList().add(found);
        }
        found.setFeatureList(dtoFeatureList);

        return this.rolesRepo.save(found);
    }

    public void deleteRole(long idToDelete){
        Role roleToDelete = this.rolesRepo.findById(idToDelete)
                .orElseThrow(() -> new NotFoundException("Role with id " + idToDelete + " not found!" ));

        for (Feature feature : roleToDelete.getFeatureList()) {
            feature.getRoleList().remove(roleToDelete);
        }

        roleToDelete.getFeatureList().clear();

        this.rolesRepo.save(roleToDelete);

        this.rolesRepo.delete(roleToDelete);
    }

    private boolean isFoundEqualsToDTO(Role found, RoleDTO featureDTO, List<Feature> dtoFeatureList){
        return found.getName().equals(featureDTO.name()) &&
                found.getFeatureList().equals(dtoFeatureList);
    }

}
