package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserTransactionCategoryDTO(

        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "isExpense must be set!")
        Boolean isExpense,
        @NotNull(message = "User id must be set!")
        String user_id
){
}
