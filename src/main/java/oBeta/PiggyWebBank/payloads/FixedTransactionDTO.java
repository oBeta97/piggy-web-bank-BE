package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FixedTransactionDTO(
        @NotEmpty(message = "Name name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        Double amount,
        @NotNull(message = "Period must be set!")
        short period,
        @NotNull(message = "User id must be set!")
        UUID user_id
) {}
