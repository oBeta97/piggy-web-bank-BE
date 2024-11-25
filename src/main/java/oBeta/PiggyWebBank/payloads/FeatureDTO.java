package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FeatureDTO(
        @NotEmpty(message = "Feature name must be set!")
        String name
) {}
