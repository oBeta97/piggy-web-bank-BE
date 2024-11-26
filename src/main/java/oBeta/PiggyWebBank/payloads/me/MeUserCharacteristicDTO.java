package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MeUserCharacteristicDTO(
        @NotEmpty(message = "Avatar must be set!")
        String avatar,
        @NotEmpty(message = "Currency must be set!")
        String currency,
        @NotNull(message = "Minimum Savings must be set!")
        double minimumSavings
) {
}
