package oBeta.PiggyWebBank.payloads.admin;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.payloads.me.MeGoalDTO;
import oBeta.PiggyWebBank.regex.Regex;

import java.time.LocalDate;

public record GoalDTO(
        @NotEmpty(message = "Name must be set!")
        String name,

        @NotNull(message = "Amount must be set!")
        double amount,
        @FutureOrPresent(message = "Expirity must be in the future!")
        LocalDate expirityDt,
        @NotNull(message = "User UUID must be set!")
        @Pattern(regexp = Regex.UUID, message = "Invalid UUID format!")
        String user_id
) {}
