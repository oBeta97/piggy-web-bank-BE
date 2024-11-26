package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;


public record MeFixedTransactionDTO(
        @NotEmpty(message = "Name name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        double amount,
        @NotNull(message = "Period must be set!")
        @Positive(message = "Period must be more than 0!")
        short period
) {}
