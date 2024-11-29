package oBeta.PiggyWebBank.controllers.admin;


import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.admin.UserCharacteristicDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.UserCharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-characteristics")
@PreAuthorize("hasAnyAuthority(" +
        "'user-characteristic:CRUD'," +
        "'user-characteristic:C'" +
        "'user-characteristic:R'," +
        "'user-characteristic:U'," +
        "'user-characteristic:D'," +
        ")"
)
public class UserCharacteristicsController {

    @Autowired
    private UserCharacteristicsService userCharacteristicsService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user-characteristic:CRUD', 'user-characteristic:R')")
    public Page<UserCharacteristic> getAllUserCharacteristics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.userCharacteristicsService.getAllUserCharacteristics(page, size, sortBy);
    }

    @GetMapping("/{userCharacteristicId}")
    @PreAuthorize("hasAnyAuthority('user-characteristic:CRUD', 'user-characteristic:R')")
    public UserCharacteristic getUserCharacteristicById(
            @PathVariable long userCharacteristicId)
    {
        return this.userCharacteristicsService.getUserCharacteristicById(userCharacteristicId);
    }

    @PutMapping("/{userCharacteristicId}")
    @PreAuthorize("hasAnyAuthority('user-characteristic:CRUD', 'user-characteristic:U')")
    public UserCharacteristic updateFeature(
            @PathVariable long userCharacteristicId,
            @RequestBody @Validated UserCharacteristicDTO body,
            BindingResult validationResult
    ){
        this.validationControl.checkErrors(validationResult);

        return this.userCharacteristicsService.updateUserCharacteristic(body);
    }
}
