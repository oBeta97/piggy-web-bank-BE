package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record UpdateMeMinimumSavingsDTO(

    @Positive(message = "Minimum savings must be positive!")
    double minimumSavings

) {}
