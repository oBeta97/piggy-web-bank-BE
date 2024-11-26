package oBeta.PiggyWebBank.payloads.developer;

import jakarta.validation.constraints.NotEmpty;

public record FeatureDTO(
        @NotEmpty(message = "Feature name must be set!")
        String name
) {}
