package oBeta.PiggyWebBank.controllers;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.FeatureDTO;
import oBeta.PiggyWebBank.services.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        return this.featuresService.getAllFeatures(page, size, sortBy);
    }

    @PostMapping
    public Feature saveNewFeature(@RequestBody @Validated FeatureDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload della fattura!");
        }

        return this.featuresService.saveNewFeature(body);
    }

}
