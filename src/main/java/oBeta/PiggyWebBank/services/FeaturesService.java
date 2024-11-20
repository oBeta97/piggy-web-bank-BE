package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.FeatureDTO;
import oBeta.PiggyWebBank.repositories.FeaturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeaturesService {

    @Autowired
    private FeaturesRepository featuresRepo;

    @Autowired
    private RolesService rolesService;

    public Page<Feature> getAllFeatures(int page, int size, String sortBy) {
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.featuresRepo.findAll(pageable);
    }

    public Feature getFeatureById (long idToFind){
        return this.featuresRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("Feature with id " + idToFind + " not found!")
                );
    }

    public Feature saveNewFeature(FeatureDTO featureDTO){

        this.featuresRepo.findByName(featureDTO.name())
            .ifPresent(feature -> {
                    throw new BadRequestException("Feature with name " + feature.getName() + " already exist!");
            });

        List<Role> roleList = this.rolesService.getRolesByList(featureDTO.roleList());

        return this.featuresRepo.save(new Feature(featureDTO.name(), roleList));
    }

    public Feature updateFeature (long idToUpdate, FeatureDTO featureDTO){

        Feature found = this.getFeatureById(idToUpdate);

        found.setName(featureDTO.name());
        List<Role> roleList = this.rolesService.getRolesByList(featureDTO.roleList());
        found.setRoleList(roleList);

        return this.featuresRepo.save(found);
    }

    public void deleteFeature (long idToDelete){
        this.featuresRepo.delete(
                this.getFeatureById(idToDelete)
        );
    }


}
