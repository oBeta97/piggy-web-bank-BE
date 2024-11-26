package oBeta.PiggyWebBank.payloads.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record VariableTransactionDTO(
        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        double amount,
        @NotNull(message = "Transaction dt must be set!")
        @PastOrPresent(message = "Transaction can't be in the future")
        LocalDateTime transactionDt,
        @NotNull(message = "Transaction category Id must be set!")
        long transactionCategory_id,
        @NotNull(message = "User id must be set!")
        @Pattern(regexp = "^[a-fA-F0-9\\-]{36}$", message = "Invalid UUID format!")
        String user_id
) {
}
