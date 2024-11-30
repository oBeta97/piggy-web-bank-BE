package oBeta.PiggyWebBank.controllers.developer;

import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.developer.FeatureDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/features")
@PreAuthorize("hasAnyAuthority(" +
        "'feature:CRUD'," +
        "'feature:C'" +
        "'feature:R'," +
        "'feature:U'," +
        "'feature:D'," +
        ")"
)
public class FeaturesController {

    @Autowired
    private FeaturesService featuresService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('feature:CRUD', 'feature:R')")
    public Page<Feature> getAllFeatures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.featuresService.getPageOfAllFeatures(page, size, sortBy);
    }

    @GetMapping("/{featureId}")
    @PreAuthorize("hasAnyAuthority('feature:CRUD', 'feature:R')")
    public Feature getFeatureById(@PathVariable long featureId){
        return this.featuresService.getFeatureById(featureId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('feature:CRUD', 'feature:C')")
    public Feature saveNewFeature(@RequestBody @Validated FeatureDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.featuresService.saveNewFeature(body);
    }

    @PutMapping("/{featureId}")
    @PreAuthorize("hasAnyAuthority('feature:CRUD', 'feature:U')")
    public Feature updateFeature(@PathVariable long featureId, @RequestBody @Validated FeatureDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.featuresService.updateFeature(featureId, body);
    }

    @DeleteMapping("/{featureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('feature:CRUD', 'feature:D')")
    public void deleteFeature(@PathVariable long featureId){
        this.featuresService.deleteFeature(featureId);
    }

}
