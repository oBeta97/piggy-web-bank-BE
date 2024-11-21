package oBeta.PiggyWebBank.payloads;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import oBeta.PiggyWebBank.entities.User;

import java.util.UUID;

public record TransactionCategoryDTO(

        @NotEmpty(message = "Name must be set!")
        String name,
        @NotNull(message = "isExpense must be set!")
        Boolean isExpense,
        @NotNull(message = "User id must be set!")
        UUID user_id
){
}
