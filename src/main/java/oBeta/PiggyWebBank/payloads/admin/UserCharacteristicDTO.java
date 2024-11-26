package oBeta.PiggyWebBank.payloads.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserCharacteristicDTO(
        @NotEmpty(message = "Avatar must be set!")
        String avatar,
        @NotEmpty(message = "Currency must be set!")
        String currency,
        @NotNull(message = "Minimum Savings must be set!")
        double minimumSavings,
        @NotNull(message = "User UUID must be set!")
        @Pattern(regexp = "^[a-fA-F0-9\\-]{36}$", message = "Invalid UUID format!")
        String user_id
) {
}
