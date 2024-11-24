package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FeatureDTO(
        @NotEmpty(message = "Feature name must be set!")
        String name,
        @NotNull(message = "Role list must be set!")
        // it can be empty list cause a feature initially is not linked to a role
        List<Long> roleList
) {}
