package oBeta.PiggyWebBank.controllers.developer;

import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.developer.RoleDTO;
import oBeta.PiggyWebBank.security.ValidationControl;
import oBeta.PiggyWebBank.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAnyAuthority(" +
        "'role:CRUD'," +
        "'role:C'" +
        "'role:R'," +
        "'role:U'," +
        "'role:D'," +
        ")"
)
public class RoleController {

    @Autowired
    private RolesService rolesService;

    @Autowired
    private ValidationControl validationControl;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('role:CRUD', 'role:R')")
    public Page<Role> getAllFeatures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.rolesService.getAllRoles(page, size, sortBy);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('role:CRUD', 'role:R')")
    public Role getRoleById(@PathVariable long roleId){
        return this.rolesService.getRoleById(roleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('role:CRUD', 'role:C')")
    public Role addNewRole(@RequestBody @Validated RoleDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.rolesService.saveNewRole(body);
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('role:CRUD', 'role:U')")
    public Role updateRole(@PathVariable long roleId, @RequestBody @Validated RoleDTO body, BindingResult validationResult){
        this.validationControl.checkErrors(validationResult);

        return this.rolesService.updateRole(roleId, body);

    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('role:CRUD', 'role:D')")
    public void deleteRole(@PathVariable long roleId){
        this.rolesService.deleteRole(roleId);
    }

}
