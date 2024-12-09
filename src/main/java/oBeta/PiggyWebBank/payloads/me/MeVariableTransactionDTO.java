package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record MeVariableTransactionDTO(
        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        double amount,
        @NotNull(message = "Transaction date time must be set!")
        @PastOrPresent(message = "Transaction date time can't be in the future")
        LocalDateTime transactionDt,
        @NotNull(message = "Transaction category Id must be set!")
        long transactionCategory_id
) {
}
