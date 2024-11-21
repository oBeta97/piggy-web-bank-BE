package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BaseTransactionCategoryDTO(
        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "isExpense must be set!")
        Boolean isExpense
) {
}
