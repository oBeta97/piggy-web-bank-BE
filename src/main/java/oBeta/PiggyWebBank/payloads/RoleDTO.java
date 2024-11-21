package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleDTO(
        @NotEmpty(message = "Role name must be set!")
        String name,
        @NotNull(message = "Feature list must be set!")
        List<Long> featureList
) {}
