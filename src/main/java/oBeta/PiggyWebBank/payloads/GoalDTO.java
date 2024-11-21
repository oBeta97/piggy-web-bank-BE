package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record GoalDTO(
        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "Period must be set!")
        Short period,
        @NotNull(message = "Amount must be set!")
        Double amount,
        @NotNull(message = "User UUID must be set!")
        UUID user_id
) {}
