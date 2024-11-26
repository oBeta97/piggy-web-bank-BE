package oBeta.PiggyWebBank.payloads.admin;

import jakarta.validation.constraints.*;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.interfaces.UsersEntity;

import java.util.UUID;


public record FixedTransactionDTO (
        @NotEmpty(message = "Name name must be set!")
        String name,
        @NotNull(message = "Amount must be set!")
        double amount,
        @NotNull(message = "Period must be set!")
        @Positive(message = "Period must be more than 0!")
        short period,
        @NotNull(message = "User id must be set!")
        @Pattern(regexp = "^[a-fA-F0-9\\-]{36}$", message = "Invalid UUID format!")
        String user_id
) implements UsersEntity {

        @Override
        public UUID getUserId() {
                try {
                        return UUID.fromString(this.user_id);
                } catch (IllegalArgumentException e) {
                        throw new BadRequestException("User id format not valid!");
                }
        }
}
