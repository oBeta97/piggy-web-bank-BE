package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MeGoalDTO(
        @NotEmpty(message = "Name must be set!")
        String name,

        @NotNull(message = "Amount must be set!")
        double amount,
        @FutureOrPresent(message = "Expirity must be in the future!")
        LocalDate expirityDt
) {}
