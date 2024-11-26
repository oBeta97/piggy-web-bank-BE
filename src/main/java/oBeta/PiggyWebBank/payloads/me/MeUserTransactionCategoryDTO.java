package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MeUserTransactionCategoryDTO(

        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "isExpense must be set!")
        Boolean isExpense
){
}
