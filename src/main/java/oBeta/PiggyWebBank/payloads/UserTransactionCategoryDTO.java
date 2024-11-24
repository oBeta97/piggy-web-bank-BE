package oBeta.PiggyWebBank.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UserTransactionCategoryDTO(

        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "isExpense must be set!")
        Boolean isExpense,
        @NotNull(message = "User id must be set!")
        @Pattern(regexp = "^[a-fA-F0-9\\-]{36}$", message = "Invalid UUID format!")
        String user_id
){
}
