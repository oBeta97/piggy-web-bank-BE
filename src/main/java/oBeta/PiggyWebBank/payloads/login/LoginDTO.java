package oBeta.PiggyWebBank.payloads.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import oBeta.PiggyWebBank.regex.Regex;

public record LoginDTO(
        @NotEmpty(message = "Username must be set!")
        String username,
        @NotEmpty(message = "Password must be set!")
        @Pattern(
                regexp = Regex.PASSWORD,
                message = "The password is not secure enough!"
        )
        String password
) {
}
