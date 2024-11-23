package oBeta.PiggyWebBank.controllers;


import oBeta.PiggyWebBank.entities.Feature;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.FeatureDTO;
import oBeta.PiggyWebBank.payloads.UserCharacteristicDTO;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-characteristics")
public class UserCharacteristicsController {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @GetMapping
    public Page<UserCharacteristic> getAllUserCharacteristics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.userCharacteristicsService.getAllUserCharacteristics(page, size, sortBy);
    }

    @GetMapping("/{userCharacteristicId}")
    public UserCharacteristic getUserCharacteristicById(@PathVariable long userCharacteristicId){
        return this.userCharacteristicsService.getUserCharacteristicById(userCharacteristicId);
    }

    @PutMapping("/{userCharacteristicId}")
    // TODO - Lo user lo prendiamo dal JWT!
    public UserCharacteristic updateFeature(@PathVariable long userCharacteristicId, @RequestBody @Validated UserCharacteristicDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.userCharacteristicsService.updateUserCharacteristic(body);
    }
}
