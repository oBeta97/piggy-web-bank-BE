package oBeta.PiggyWebBank.controllers;

import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.payloads.RoleDTO;
import oBeta.PiggyWebBank.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RolesService rolesService;

    @GetMapping
    public Page<Role> getAllFeatures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        return this.rolesService.getAllRoles(page, size, sortBy);
    }

    @GetMapping("/{roleId}")
    public Role getRoleById(@PathVariable long roleId){
        return this.rolesService.getRoleById(roleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // ADMIN can update a Role!
    public Role addNewRole(@RequestBody @Validated RoleDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.rolesService.saveNewRole(body);
    }

    @PutMapping("/{roleId}")
    // ADMIN can update a Role!
    public Role updateRole(@PathVariable long roleId, @RequestBody @Validated RoleDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }

        return this.rolesService.updateRole(roleId, body);

    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // ADMIN can delete roles!
    public void deleteRole(@PathVariable long roleId){
        this.rolesService.deleteRole(roleId);
    }

}
