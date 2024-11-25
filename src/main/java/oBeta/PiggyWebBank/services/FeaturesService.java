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

import java.util.ArrayList;
import java.util.List;


@Service
public class FeaturesService {

    @Autowired
    private FeaturesRepository featuresRepo;

    @Autowired
    private RolesService rolesService;

    public Page<Feature> getPageOfAllFeatures(int page, int size, String sortBy) {
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.featuresRepo.findAll(pageable);
    }

    public List<Feature> getAllFeatures(){
        return this.featuresRepo.findAll();
    }

    public Feature getFeatureById (long idToFind){
        return this.featuresRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("Feature with id " + idToFind + " not found!")
                );
    }

    public List<Feature> getFeaturesByList(List<Long> idList){

        List<Feature> res = new ArrayList<>();

        for (long idToFind : idList){
            res.add(this.getFeatureById(idToFind));
        }

        return res;
    }

    public Feature saveNewFeature(FeatureDTO featureDTO){

        this.featuresRepo.findByName(featureDTO.name())
            .ifPresent(feature -> {
                    throw new BadRequestException("Feature with name " + feature.getName() + " already exist!");
            });

        return this.featuresRepo.save(new Feature(featureDTO.name()));
    }

    public Feature updateFeature (long idToUpdate, FeatureDTO featureDTO){

        Feature found = this.getFeatureById(idToUpdate);

        this.featuresRepo.findByName(featureDTO.name())
            .ifPresent( feature -> {
                if(feature.getId() != idToUpdate)
                    throw new BadRequestException("Feature with name " + feature.getName() + " already exist!");
            }
        );

        // if there's not changes the update won't be done
        if(this.isFoundEqualsToDTO(found, featureDTO))
            return found;

        found.setName(featureDTO.name());

        return this.featuresRepo.save(found);
    }

    public void deleteFeature (long idToDelete){
        Feature featureToDelete = this.featuresRepo.findById(idToDelete)
                .orElseThrow(() ->
                        new NotFoundException("Feature with id " + idToDelete + " not found!")
                );

        for (Role role : featureToDelete.getRoleList()) {
            role.getFeatureList().remove(featureToDelete);
        }

        featureToDelete.getRoleList().clear();

        this.featuresRepo.save(featureToDelete);

        this.featuresRepo.delete(featureToDelete);
    }

    private boolean isFoundEqualsToDTO(Feature found, FeatureDTO featureDTO){
        return found.getName().equals(featureDTO.name());
    }

}
