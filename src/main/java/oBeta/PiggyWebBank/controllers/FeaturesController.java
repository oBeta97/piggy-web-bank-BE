package oBeta.PiggyWebBank.controllers;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.FeatureDTO;
import oBeta.PiggyWebBank.services.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/features")
public class FeaturesController {

    @Autowired
    private FeaturesService featuresService;

    @GetMapping
    public Page<Feature> getAllFeatures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.featuresService.getPageOfAllFeatures(page, size, sortBy);
    }

    @GetMapping("/{featureId}")
    public Feature getFeatureById(@PathVariable long featureId){
        return this.featuresService.getFeatureById(featureId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // ONLY DEV can save a new Feature!
    public Feature saveNewFeature(@RequestBody @Validated FeatureDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.featuresService.saveNewFeature(body);
    }

    @PutMapping("/{featureId}")
    // ONLY DEV can update a Feature!
    public Feature updateFeature(@PathVariable long featureId, @RequestBody @Validated FeatureDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.featuresService.updateFeature(featureId, body);
    }

    @DeleteMapping("/{featureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // ONLY DEV can delete a Feature!
    public void deleteFeature(@PathVariable long featureId){
        this.featuresService.deleteFeature(featureId);
    }

}
