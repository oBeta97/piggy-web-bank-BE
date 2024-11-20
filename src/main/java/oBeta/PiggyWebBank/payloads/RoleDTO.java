package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleDTO(
        @NotEmpty(message = "Feature name must be set!")
        String name,
        @NotNull(message = "User id must be set!")
        long userId,
        @NotNull(message = "Role id must be set!")
        List<Long> roleList
) {}
