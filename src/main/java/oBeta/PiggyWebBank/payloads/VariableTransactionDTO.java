package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.UUID;

public record VariableTransactionDTO(
        @NotEmpty(message = "Name name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        double amount,
        @NotNull(message = "Transaction dt must be set!")
        @PastOrPresent(message = "Transaction can't be in the future")
        LocalDateTime transactionDt,
        @NotNull(message = "Transaction category Id must be set!")
        long transactionCategory_id,
        @NotNull(message = "User id must be set!")
        UUID user_id
) {
}
