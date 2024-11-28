package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import oBeta.PiggyWebBank.regex.Regex;

public record MeUserDTO(

        String name,
        String surname,
        @NotEmpty(message = "Username must be set!")
        String username,
        @NotEmpty(message = "Email must be set!")
        @Pattern(regexp = Regex.EMAIL, message = "The email address you entered is invalid!")
        String email

) {
}
